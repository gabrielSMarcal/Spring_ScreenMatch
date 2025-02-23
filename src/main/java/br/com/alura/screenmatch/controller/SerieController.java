package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService servico;

    @GetMapping
    public List<SerieDTO> buscarSeries() {
        return servico.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> buscarTop5Series() {
        return servico.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> buscarLancamentos() {
        return servico.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO buscarPorId(@PathVariable Long id) {
        return servico.obterPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTemporadas(@PathVariable Long id) {
        return servico.obterTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTempotradasPorNumero(@PathVariable Long id, @PathVariable Long numero) {
        return servico.obterTemporadasPorNumero(id, numero);
    }

    @GetMapping("/categoria/{genero}")
    public List<SerieDTO> buscarPorGenero(@PathVariable String genero) {
        return servico.obterPorGenero(genero);
    }
}
