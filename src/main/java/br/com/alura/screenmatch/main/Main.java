package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.service.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Main {

    private Scanner leitura = new Scanner(System.in);

    private ConsumoApi api = new ConsumoApi();
    private ConverterDados conversor = new ConverterDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=987702fa";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    public void menu() {

        var opcao = -1;

        while (opcao != 0) {

            var menu = """
                                
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
                
                0 - Sair
                """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    procurarSerieWeb();
                    break;
                case 2:
                    episodioProcuradoPorSerie();
                    break;
                case 3:
                    listarSeriesProcuradas();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void procurarSerieWeb() {

        DadosSerie dado = getDadosSerie();
        dadosSeries.add(dado);
        System.out.println(dado);
    }

    private DadosSerie getDadosSerie() {

        System.out.println("Digite o nome da série para busca: ");

        var nomeSerie = leitura.nextLine();
        var json = api.getDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dado = conversor.getDados(json, DadosSerie.class);
        return dado;
    }

    private void episodioProcuradoPorSerie(){

        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {

            var json = api.getDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.getDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void listarSeriesProcuradas() {

        List<Serie> series = dadosSeries.stream()
                .map(d -> new Serie(d))
                .collect(Collectors.toList());
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }


}
