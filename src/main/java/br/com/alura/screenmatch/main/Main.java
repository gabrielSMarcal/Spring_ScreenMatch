package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.service.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner scanner = new Scanner(System.in);

    private ApiConsumer api = new ApiConsumer();
    private ConvertsData conversor = new ConvertsData();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=987702fa";

    public void menu() {

        System.out.println("Bem-vindo ao Screen Match!");
        System.out.println("*****************************************************************************************");
        System.out.println("Digite o nome da série que deseja buscar:");

        var serieName = scanner.nextLine();
        var json = api.getData(ADDRESS + serieName.replace(" ", "+") + API_KEY);
        var data = conversor.getData(json, SerieData.class);
        System.out.println(data);
        System.out.println("*****************************************************************************************");

        List<SeasonData> seasons = new ArrayList<>();
        for (int i = 1; i <= data.totalTemporadas(); i++) {

            json = api.getData(ADDRESS + serieName.replace(" ", "+") +
                    "&Season=" + i + API_KEY);
            SeasonData season = conversor.getData(json, SeasonData.class);
            seasons.add(season);
        }
//        seasons.forEach(System.out::println);
//        System.out.println("*****************************************************************************************");

//        for (int i = 0; i < data.totalTemporadas(); i++) {
//
//            List<EpisodeData> seasonEpisodes = seasons.get(i).episodios();
//            for (int j = 0; j < seasonEpisodes.size(); j++) {
//                System.out.println(seasonEpisodes.get(j).titulo());
//            }
//        }

        // Lambda expression
        // seasons.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<EpisodeData> episodesData = seasons.stream()
                .flatMap(s -> s.episodios().stream())
                .collect(Collectors.toList());

//        System.out.println("\n10 melhores episódios:");
//        episodesData.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro(N/A)" + e))
//                .sorted(Comparator.comparing(EpisodeData::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenação: " + e))
//                .limit(10)
//                .peek(e -> System.out.println("Limitação: " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Mapeamento: " + e))
//                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(s -> s.episodios().stream()
                        .map(d -> new Episode(s.temporada(), d))
                ).collect(Collectors.toList());

//        episodes.forEach(System.out::println);
//
//        System.out.println("Procure episódio pelo título:");
//        var titlePart = scanner.nextLine();
//        Optional<Episode> searchEpisode = episodes.stream()
//                .filter(e -> e.getTitle().toUpperCase().contains(titlePart.toUpperCase()))
//                .findFirst();
//
//        if (searchEpisode.isPresent()) {
//            System.out.println("Episódio encontrado: " + searchEpisode.get());
//        } else {
//            System.out.println("Episódio não encontrado.");
//        }


//        System.out.println("A partir de que ano deseja buscar episódios?");
//        var year = scanner.nextInt();
//        scanner.nextLine();
//
//        LocalDate dateSearch = LocalDate.of(year, 1, 1);
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodes.stream()
//                .filter(e -> e.getLaunch() != null && e.getLaunch().isAfter(dateSearch))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getSeason() +
//                                " - Episódio: " + e.getEpisodeNumber() +
//                                " - Data de lançamento: " + e.getLaunch().format(dtf)
//                ));

        System.out.println("*****************************************************************************************");

        Map<Integer, Double> averageRating = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(
                        Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)
                ));

        System.out.println("Média de avaliação por temporada:" + averageRating);

        DoubleSummaryStatistics est = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(
                        Episode::getRating
                ));

        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Total de episódios: " + est.getCount());
    }
}
