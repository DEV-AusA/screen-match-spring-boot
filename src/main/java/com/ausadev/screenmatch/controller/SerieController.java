package com.ausadev.screenmatch.controller;

import com.ausadev.screenmatch.dto.SerieDTO;
import com.ausadev.screenmatch.model.Serie;
import com.ausadev.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {

    @Autowired
    private SerieRepository repository;

    @GetMapping("/series")
    public List<SerieDTO> obtenerTodasLasSeries(){
        return repository.findAll().stream()
                .map(s -> new SerieDTO(s.getTitulo(), s.getTotalDeTemporadas(), s.getEvaluacion(), s.getGenero(), s.getSinopsis(), s.getPoster(), s.getActores()))
                .collect(Collectors.toList());
    }
}
