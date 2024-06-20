package com.ausadev.screenmatch.principal;

import com.ausadev.screenmatch.model.DatosSerie;
import com.ausadev.screenmatch.model.DatosTemporadas;
import com.ausadev.screenmatch.service.ConsumoAPI;
import com.ausadev.screenmatch.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=364e5920";
    ConsumoAPI consumoApi = new ConsumoAPI();
    ConvierteDatos convierteDatos = new ConvierteDatos();

    public void mostrarMenu() {
        System.out.println("Ingrese el nombre de la seria a buscar");
        var nombre = teclado.nextLine();

        // obtengo los datos de la serie que ingresa el usuario
        var json = consumoApi.obtenerDatos(URL_BASE + nombre.replace(" ", "+") + API_KEY);
        var datos = convierteDatos.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        // obtengo los datos de cada capitulo de cada serie
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i < datos.totalDeTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE + nombre.replace(" ", "+") + "&Season=" + i + API_KEY);
            var datosTemporadas = convierteDatos.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporadas);
        }
        temporadas.forEach(System.out::println);
    }
}
