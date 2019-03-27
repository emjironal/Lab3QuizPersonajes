package com.example.lab3quizpersonajesapp;

import android.os.AsyncTask;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String>
{
    @Override
    protected String doInBackground(String... urls)
    {

        String result = "";
        URL url;

        // Agregar permiso en AndroidManifest.xml
        HttpURLConnection urlConnection = null;

        try
        {
            url = new URL(urls[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            // Esto es muy estilo C
            // Se lee un caracter a la vez (como cuando se hace gets() en C o C++)
            int data = inputStreamReader.read();
            while (data != -1){
                char character = (char)data;
                result += character;
                data = inputStreamReader.read();
            }

            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Error";
        }
    }
}
