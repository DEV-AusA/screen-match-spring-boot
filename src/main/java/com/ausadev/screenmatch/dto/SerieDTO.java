package com.ausadev.screenmatch.dto;

import com.ausadev.screenmatch.model.Categoria;

public record SerieDTO(
        Long id,
        String titulo,
        Integer totalDeTemporadas,
        Double evaluacion,
        Categoria genero,
        String sinopsis,
        String poster,
        String actores
) {
}
