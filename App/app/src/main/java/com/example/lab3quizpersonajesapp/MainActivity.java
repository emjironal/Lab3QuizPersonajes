package com.example.lab3quizpersonajesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
{

    private Elements personajes;
    private Elements imagenes;
    private final String paginaTop50PersonajesAnime = "https://listas.20minutos.es/lista/los-50-mejores-personajes-del-anime-404070/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Descargo la página
        DownloadTask downloadTask = new DownloadTask();
        String resultado = "";
        /*try
        {
            resultado = downloadTask.execute(paginaTop50PersonajesAnime).get();
        }
        catch (ExecutionException e)
        {
            Log.e("Error", e.getMessage());
        }
        catch (InterruptedException e)
        {
            Log.e("Error", e.getMessage());
        }*/
        resultado = HTML.html;
        if(!resultado.equals("Error"))
        {
            parseHtml(resultado);
            Toast.makeText(this, "exito " + personajes.size() + " " + imagenes.size(), Toast.LENGTH_LONG).show();
        }
        ListView listPersonajes = findViewById(R.id.listViewOptions);
        ArrayList<String> per = new ArrayList<>();
        for(Element e : personajes)
        {
            per.add(e.text());
        }
        ArrayAdapter<String>  adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, per);
        listPersonajes.setAdapter(adapter);
    }

    private void parseHtml(String html)
    {
        Document doc = Jsoup.parse(html);
        personajes = doc.select("div .info h3"); //obtiene los nombres
        imagenes = doc.select("p .fotolista"); //obtiene las imagenes
    }

    private String getNombrePersonaje(int id)
    {
        return personajes.get(id).text();
    }

    private String getImagenPersonaje(int id)
    {
        return imagenes.get(id).attr("href");
    }
}
//https://listas.20minutos.es/lista/los-50-mejores-personajes-del-anime-404070/
