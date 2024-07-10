package com.ausadev.screenmatch.principal;

import com.ausadev.screenmatch.model.*;
import com.ausadev.screenmatch.repository.SerieRepository;
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
    List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    List<Serie> series;

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar serie por titulo
                    5 - Top 5 mejores series
                    6 - Buscar series por categoria
                    7 - Filtrar serie por temporada y evaluacion
                    8 - Buscar episodio por titulo
                                                      
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodioPorTitulo();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private void buscarEpisodioPorTitulo() {
        System.out.println("Ingrese el nombre del episodio a buscar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);

        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie: %s Temporada %s Episodio %s Evaluacion %s \n",
                        e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()
                ));
    }

    private void filtrarSeriesPorTemporadaYEvaluacion() {
        System.out.println("¿Filtrar series con cuantas temporadas?");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Con que evaluacion deseas filtrar?");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaYEvaluacion(totalTemporadas, evaluacion);
        System.out.println("******* Series filtradas ********");
        filtroSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + " " + "Evaluacion: " + s.getEvaluacion()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Escriba el genero/categoria de la serie que desea buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Series por categoria " + genero + ":");
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarTop5Series() {
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + " " + "Evaluacion: " + s.getEvaluacion()));
    }

    private void buscarSeriesPorTitulo() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        if (serieBuscada.isPresent()){
            System.out.println("La serie bsucada es: " + serieBuscada.get());
        }
        else {
            System.out.println("Serie no encontrada");
        }
    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = convierteDatos.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Ingrese el nombre de la serie de los episodios a mostrar");
        var nombreSerie = teclado.nextLine();
        //Optional que permite retornar ALGUNOS DATOS
        Optional<Serie> serie = series.stream()
                // filtramos la serie segun el input ingresado
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
        if (serie.isPresent()){
            var serieEncontrada = serie.get();

            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalDeTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = convierteDatos.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    // d => dato
                    .flatMap(d -> d.episodios()
                            .stream()
                            // e => episodio, y cada episodio sera un new Episodio de la clase Episodio
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            // envio los episodios al set de serieEncontrada
            serieEncontrada.setEpisodios(episodios);
            // save
            repositorio.save(serieEncontrada);
        }
    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);
//        datosSeries.add(datos);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}
