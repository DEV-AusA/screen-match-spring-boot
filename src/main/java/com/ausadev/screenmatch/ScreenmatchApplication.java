package com.ausadev.screenmatch;

import com.ausadev.screenmatch.model.DatosEpisodio;
import com.ausadev.screenmatch.model.DatosSerie;
import com.ausadev.screenmatch.model.DatosTemporadas;
import com.ausadev.screenmatch.principal.EjemploStreamYLambda;
import com.ausadev.screenmatch.principal.Principal;
import com.ausadev.screenmatch.repository.SerieRepository;
import com.ausadev.screenmatch.service.ConsumoAPI;
import com.ausadev.screenmatch.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}
}
