package com.ausadev.screenmatch.principal;

import com.ausadev.screenmatch.model.DatosEpisodio;
import com.ausadev.screenmatch.model.DatosSerie;
import com.ausadev.screenmatch.model.DatosTemporadas;
import com.ausadev.screenmatch.service.ConsumoAPI;
import com.ausadev.screenmatch.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE + nombre.replace(" ", "+") + "&Season=" + i + API_KEY);
            var datosTemporadas = convierteDatos.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporadas);
        }
        temporadas.forEach(System.out::println);

//        //limpiando los datos mostrados
//        for (int i = 0; i < datos.totalDeTemporadas(); i++) {
//            //declaro la lista y como valor mediante el valor de "i" como indice dle metodo get()
//            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            //uso otro "for" y como valor de iteracion uso el metodo .size pra obtener la cantidad de episodios
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                //muestro el titulo de cada capitulo
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        //simplificando en UNA LINEA con la function lambda
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //convirtiendo las listas de temporadas en una lista de todos los episodios
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toUnmodifiableList());

        //mostrando top5
        System.out.println("Este es el top 5 con mas puntaje de episodios: ");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .limit(5)
                .forEach(System.out::println);
    }
}
