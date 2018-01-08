package com.example.laptop.texteditor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity
{
    public static final int IDM_OPEN = 101;
    public static final int IDM_SAVE = 102;
    public static final int IDM_PREF = 103;

    private final static String FILENAME = "TextFile.txt";
    private EditText myEdit;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myEdit = (EditText)findViewById(R.id.editText);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean(getString(R.string.pref_openmode), false))
        {
            openFile(FILENAME);
        }

        float fSize = Float.parseFloat(prefs.getString(getString(R.string.pref_size), "20"));
        myEdit.setTextSize(fSize);

        String regular = prefs.getString(getString(R.string.pref_style), "");
        int typeface = Typeface.NORMAL;

        if (regular.contains("Bold"))
            typeface += Typeface.BOLD;

        if (regular.contains("Italics"))
            typeface += Typeface.ITALIC;

        myEdit.setTypeface(null, typeface);

        int color = Color.BLACK;
        if (prefs.getBoolean(getString(R.string.pref_color_red), false))
            color += Color.RED;
        if (prefs.getBoolean(getString(R.string.pref_color_green), false))
            color += Color.GREEN;
        if (prefs.getBoolean(getString(R.string.pref_color_blue), false))
            color += Color.BLUE;

        myEdit.setTextColor(color);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(Menu.NONE, IDM_OPEN, Menu.NONE, "Open");
        menu.add(Menu.NONE, IDM_SAVE, Menu.NONE, "Save").setIcon(android.R.drawable.ic_menu_save);
        menu.add(Menu.NONE, IDM_PREF, Menu.NONE, "Settings...");

        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case IDM_OPEN:
                openFile(FILENAME);
                break;
            case IDM_SAVE:
                saveFile(FILENAME);
                break;
            case IDM_PREF:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                return false;
        }
        return true;
    }

    private void openFile(String fileName)
    {
        try
        {
            InputStream inStream = openFileInput(FILENAME);

            if(inStream != null)
            {
                InputStreamReader sr = new InputStreamReader(inStream);
                BufferedReader reader = new BufferedReader(sr);
                String str;
                StringBuffer buffer = new StringBuffer();

                while((str = reader.readLine()) != null)
                {
                    buffer.append(str + "\n");
                }

                inStream.close();
                myEdit.setText(buffer.toString());
            }
        }
        catch(Throwable t)
        {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void saveFile(String FileName)
    {
        try
        {
            OutputStream outStream = openFileOutput(FILENAME, 0);
            OutputStreamWriter sw = new OutputStreamWriter(outStream);
            sw.write(myEdit.getText().toString());
            sw.close();
        }
        catch (Throwable t)
        {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }
}