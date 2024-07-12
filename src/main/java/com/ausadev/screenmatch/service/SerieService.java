package com.ausadev.screenmatch.service;

import com.ausadev.screenmatch.dto.SerieDTO;
import com.ausadev.screenmatch.model.Serie;
import com.ausadev.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obtenerTodasLasSeries(){
        return convierteDatos(repository.findAll());
    }

    public List<SerieDTO> obtenerTop5() {
        return convierteDatos(repository.findTop5ByOrderByEvaluacionDesc());
    }

    public List<SerieDTO> obtenerLanzamiantosMasRecientes() {
        return convierteDatos(repository.lanzamientosMasReciente());
    }

    public List<SerieDTO> convierteDatos(List<Serie> serie) {
        return serie.stream()
                .map(s -> new SerieDTO(s.getTitulo(), s.getTotalDeTemporadas(), s.getEvaluacion(), s.getGenero(), s.getSinopsis(), s.getPoster(), s.getActores()))
                .collect(Collectors.toList());
    }
}
