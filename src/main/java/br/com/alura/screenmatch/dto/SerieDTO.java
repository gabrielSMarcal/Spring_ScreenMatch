package br.com.alura.screenmatch.dto;

public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       br.com.alura.screenmatch.model.Categoria genero,
                       String atores,
                       String poster,
                       String sinopse) {
}
