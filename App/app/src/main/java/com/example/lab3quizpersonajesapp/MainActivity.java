package com.example.lab3quizpersonajesapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.shuffle;

public class MainActivity extends AppCompatActivity
{

    private Elements personajes;
    private Elements imagenes;
    private final String paginaTop50PersonajesAnime = "https://listas.20minutos.es/lista/los-50-mejores-personajes-del-anime-404070/";
    private int currentPersonaje;
    private int currentPersonajePos;
    private ArrayList<Integer> ordenPersonajes = new ArrayList<>();

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
        }
        */resultado = HTML.html;
        if(!resultado.equals("Error"))
        {
            parseHtml(resultado);
        }
        setPersonaje();
        ListView listViewPersonajes = findViewById(R.id.listViewOptions);
        listViewPersonajes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(position == currentPersonajePos)
                {
                    Toast.makeText(getApplicationContext(), "Respuesta correcta", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
                }
                setPersonaje();
            }
        });
    }

    private void setOrdenPersonajes()
    {
        for(int i = 0; i < 50; i++)
        {
            ordenPersonajes.add(i);
        }
        shuffle(ordenPersonajes);
    }

    private void setPersonaje()
    {
        ArrayList<String> listPersonajes = getListaPersonajesPregunta();
        ArrayAdapter<String> adapter = getAdapter(listPersonajes);
        ListView listView = findViewById(R.id.listViewOptions);
        listView.setAdapter(adapter);
        ImageView imageView = findViewById(R.id.imageViewPersonaje);
        try
        {
            imageView.setImageBitmap((new ImageDownloader().execute(getImagenPersonaje(currentPersonaje)).get()));
        }
        catch (ExecutionException e)
        {
            Log.e("Error", e.getMessage());
        }
        catch (InterruptedException e)
        {
            Log.e("Error", e.getMessage());
        }
    }

    private ArrayAdapter<String> getAdapter(ArrayList<String> array)
    {
        return new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array);
    }

    private ArrayList<String> getListaPersonajesPregunta()
    {
        Random random = new Random();
        if(ordenPersonajes.isEmpty())
        {
            setOrdenPersonajes();
        }
        currentPersonaje = ordenPersonajes.get(0);
        ordenPersonajes.remove(0);
        int idTrampa1, idTrampa2, idTrampa3;
        do {idTrampa1 = random.nextInt(50);} while(idTrampa1 == currentPersonaje);
        do {idTrampa2 = random.nextInt(50);} while(idTrampa2 == currentPersonaje || idTrampa2 == idTrampa1);
        do {idTrampa3 = random.nextInt(50);} while(idTrampa3 == currentPersonaje || idTrampa3 == idTrampa1 || idTrampa3 == idTrampa2);
        ArrayList<String> personajes = new ArrayList<>();
        personajes.add(getNombrePersonaje(currentPersonaje));
        personajes.add(getNombrePersonaje(idTrampa1));
        personajes.add(getNombrePersonaje(idTrampa2));
        personajes.add(getNombrePersonaje(idTrampa3));
        shuffle(personajes);
        currentPersonajePos = personajes.indexOf(getNombrePersonaje(currentPersonaje));
        return personajes;
    }

    private void parseHtml(String html)
    {
        Document doc = Jsoup.parse(html);
        personajes = doc.select("div .info h3"); //obtiene los nombres
        imagenes = doc.select("div .elemento noscript img"); //obtiene las imagenes
    }

    private String getNombrePersonaje(int id)
    {
        return personajes.get(id).text().substring(3);
    }

    private String getImagenPersonaje(int id)
    {
        return imagenes.get(id).attr("src");
    }
}
//https://listas.20minutos.es/lista/los-50-mejores-personajes-del-anime-404070/
