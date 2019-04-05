package com.example.lab3quizpersonajesapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String>
{
    @Override
    protected String doInBackground(String... urls)
    {

        StringBuilder result = new StringBuilder();
        URL url;

        // Agregar permiso en AndroidManifest.xml
        HttpURLConnection urlConnection = null;

        try
        {
            Log.e("Error", "Obtiene url");
            url = new URL(urls[0]);
            Log.e("Error", "Abre la conexion");
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.e("Error", "Obtiene el html");
            InputStream inputStream = urlConnection.getInputStream();
            Log.e("Error", "Abre el html");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            // Esto es muy estilo C
            // Se lee un caracter a la vez (como cuando se hace gets() en C o C++)
            Log.e("Error", "Lee el html");
            int data = inputStreamReader.read();
            Log.e("Error", "Empieza a recorrer caracter por carater");
            while (data != -1){
                char character = (char)data;
                result.append(character);
                data = inputStreamReader.read();
                //Log.e("Error", result.toString());
            }
            Log.e("Error", "Termina de leer el html y devuelve");

            return result.toString();
        }
        catch (Exception e)
        {
            Log.e("Error", e.getMessage());
            return "Error";
        }
    }
}
