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
        seasons.forEach(System.out::println);
    }
}
