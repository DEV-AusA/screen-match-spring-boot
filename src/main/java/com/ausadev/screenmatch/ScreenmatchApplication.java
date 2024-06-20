package com.ausadev.screenmatch;

import com.ausadev.screenmatch.model.DatosSerie;
import com.ausadev.screenmatch.service.ConsumoAPI;
import com.ausadev.screenmatch.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoAPI();
		var json = consumoApi.obtenerDatos("http://www.omdbapi.com/?t=game+of+thrones&apikey=364e5920");
//		var json = consumoApi.obtenerDatos("https://api.themoviedb.org/3/movie/top_rated?api_key=223af92cc732da1c02b1cde6cb997b6e");

		System.out.println(json);

		ConvierteDatos convierteDatos = new ConvierteDatos();
		var datos = convierteDatos.obtenerDatos(json, DatosSerie.class);

		System.out.println(datos);
	}
}
