package com.example.lab3quizpersonajesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
        try
        {
            resultado = downloadTask.execute(paginaTop50PersonajesAnime).get();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        if(!resultado.equals("Error"))
        {
            parseHtml(resultado);
            Toast.makeText(this, "exito", Toast.LENGTH_LONG);
        }
    }

    private void parseHtml(String html)
    {
        Document doc = Jsoup.parse(html);
        personajes = doc.select("div .listas_elementos div .elemento div .info h3"); //obtiene los nombres
        imagenes = doc.select("div .listas_elementos div .elemento p .fotolista a"); //obtiene las imagenes
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
