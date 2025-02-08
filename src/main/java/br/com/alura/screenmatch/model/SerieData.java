package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SerieData(@JsonAlias("Title") String titulo,
                        @JsonAlias("totalSeasons") int totalTemporadas,
                        @JsonAlias("imdbRating") String avaliacao) {
}
