package br.com.alura.screenmatch.model;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Episode {

    private int season;
    private String title;
    private int episodeNumber;
    private Double rating;
    private LocalDate launch;

    public Episode(int seasonNumber, EpisodeData episodeData) {

        this.season = seasonNumber;
        this.title = episodeData.titulo();
        this.episodeNumber = episodeData.numero();
        try {
            this.rating = Double.valueOf(episodeData.avaliacao());
        } catch (NumberFormatException ex) {
            this.rating = 0.0;
        }
        try {
            this.launch = LocalDate.parse(episodeData.dataLancamento());
        } catch (DateTimeException ex) {
            this.launch = null;
        }
    }


    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getLaunch() {
        return launch;
    }

    public void setLaunch(LocalDate launch) {
        this.launch = launch;
    }

    @Override
    public String toString() {
        return "Temporada = " + season +
                ", Título = '" + title + '\'' +
                ", Episódio = " + episodeNumber +
                ", Avaliação = " + rating +
                ", Data de Lançamento = " + launch;
    }
}
