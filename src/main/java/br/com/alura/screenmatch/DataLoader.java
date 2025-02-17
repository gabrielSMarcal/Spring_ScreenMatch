package br.com.alura.screenmatch;

import br.com.alura.screenmatch.main.Main;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SerieRepository repositorio;

    @Override
    public void run(String... args) throws Exception {
        Main main = new Main(repositorio);
        List<Serie> series = repositorio.findAll();
        for (Serie serie : series) {
            main.buscarEpisodioPorSerie(serie.getTitulo());
        }
    }
}