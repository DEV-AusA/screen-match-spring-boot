package com.ausadev.screenmatch.principal;

import com.ausadev.screenmatch.model.DatosEpisodio;
import com.ausadev.screenmatch.model.DatosSerie;
import com.ausadev.screenmatch.model.DatosTemporadas;
import com.ausadev.screenmatch.model.Episodio;
import com.ausadev.screenmatch.service.ConsumoAPI;
import com.ausadev.screenmatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
                .collect(Collectors.toList());

        //mostrando top5
        System.out.println("Este es el top 5 con mas puntaje de episodios: ");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primer filtro: N/A" + e))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .peek(e -> System.out.println("Segundo filtro: ordenando" + e))
                .limit(5)
                .forEach(System.out::println);

        //convirtiendo los datos a una Lista de tipo clase Episodio
        List<Episodio> episodios = temporadas.stream()
                // convierte cada temporada a una Lista de Episodio
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        //buscando episodios por fecha ingresada
        System.out.println("Ingrese el año de filtrado");
        var fecha = teclado.nextLine();

        // LocalDate.of() recieb 3 parametros yyyy-MM-dd
        LocalDate fechaBusqueda = LocalDate.of(Integer.parseInt(fecha), 1, 1);
        //formateando la fecha a tipo LATAM dd-MM-yyyy con el metodo DateTimeFormatter.ofPattern()
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        episodios.stream()
                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                ", Episodio: " + e.getTitulo() +
                                ", Fecha de lanzamiento: " + e.getFechaDeLanzamiento().format(dtf)
                ));

        //Busqueda de titulo por ingreso parcial del usuario
        System.out.println("Ingrese el titulo o parte del titulo a buscar");
        var nombreTitulo = teclado.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                //uso el upperCase para hacer match de los que existen y el buscado
                .filter(e -> e.getTitulo().toUpperCase().contains(nombreTitulo.toUpperCase()))
                .findFirst();

        // si existe un titulo lo muestro sino muestro otro msj
        if (episodioBuscado.isPresent()) {
            System.out.println("Titulo encontrado");
            System.out.println("Datos del episodio encontrado: " + episodioBuscado.get());
        }
        else {
            System.out.println("Titulo no encontrado");
        }

    }
}
