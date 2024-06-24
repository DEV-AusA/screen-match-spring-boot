package com.ausadev.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name="series")
public class Serie {
    @Id
    @GeneratedValue
    private Long Id;
    private String titulo;
    private Integer totalDeTemporadas;
    private Double evaluacion;
    private Categoria genero;
    private String sinopsis;
    private String poster;
    private String actores;
    @Transient
    private List<Episodio> episodios;

    public Serie() {

    }

    public Serie(DatosSerie datosSerie) {
        this.titulo = datosSerie.titulo();
        this.totalDeTemporadas = datosSerie.totalDeTemporadas();
        //en lugar de hacer un try/catch utilizo el metodo OptionalDouble.of(Double.valueOf())
        //el metodo Double.valueOf() - convierte el String a Double, si no puede convertir retorna el metodo .orElse(0)
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0);
        this.poster = datosSerie.poster();
        //para el genero declaro un metodo en el Enum Categoria que transformara cada String a una categoria del Enum
        //luego de declararlo como valor coloco el Enum con su metodo "Categoria.fromString()"
        //split por la coma, tomo la posicion 0 y con trim evito espacios vacios
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
        this.sinopsis = datosSerie.sinopsis();
        this.actores = datosSerie.actores();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalDeTemporadas() {
        return totalDeTemporadas;
    }

    public void setTotalDeTemporadas(Integer totalDeTemporadas) {
        this.totalDeTemporadas = totalDeTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    @Override
    public String toString() {
        return
                "genero=" + genero +
                ", titulo='" + titulo + '\'' +
                ", totalDeTemporadas=" + totalDeTemporadas +
                ", evaluacion=" + evaluacion +
                ", sinopsis='" + sinopsis + '\'' +
                ", poster='" + poster + '\'' +
                ", actores='" + actores + '\'';
    }
}
