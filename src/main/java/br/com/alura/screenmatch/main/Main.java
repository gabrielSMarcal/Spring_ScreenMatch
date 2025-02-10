package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

    private Scanner scanner = new Scanner(System.in);

    private ApiConsumer api = new ApiConsumer();
    private ConvertsData conversor = new ConvertsData();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=987702fa";
    private List<SerieData> series = new ArrayList<>();

    public void menu() {

        var option = -1;

        while (option != 0) {

            var menu = """
                
                Bem vindo ao ScreenMatch!
                
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
                
                0 - Sair
                """;

            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    searchWebSerie();
                    break;
                case 2:
                    searchEpisodeBySerie();
                    break;
                case 3:
                    listSearchedSeries();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void searchWebSerie() {

        SerieData data = getSerieData();
        series.add(data);
        System.out.println(data);
    }

    private SerieData getSerieData() {

        System.out.println("Digite o nome da série para busca: ");

        var serieName = scanner.nextLine();
        var json = api.getData(ADDRESS + serieName.replace(" ", "+") + API_KEY);
        SerieData data = conversor.getData(json, SerieData.class);
        return data;
    }

    private void searchEpisodeBySerie(){

        SerieData serieData = getSerieData();
        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= serieData.totalTemporadas(); i++) {

            var json = api.getData(ADDRESS + serieData.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            SeasonData dadosTemporada = conversor.getData(json, SeasonData.class);
            seasons.add(dadosTemporada);
        }
        seasons.forEach(System.out::println);
    }

    private void listSearchedSeries() {

        series.forEach(System.out::println);
    }


}
