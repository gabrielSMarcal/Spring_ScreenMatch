package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.SerieData;
import br.com.alura.screenmatch.service.ApiConsumer;
import br.com.alura.screenmatch.service.ConvertsData;

import java.util.Scanner;

public class Main {

    private Scanner scanner = new Scanner(System.in);

    private ApiConsumer api = new ApiConsumer();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=987702fa";

    public void menu() {

        System.out.println("Bem-vindo ao Screen Match!");
        System.out.println("*****************************************************************************************");
        System.out.println("Digite o nome da série que deseja buscar:");
        var serieName = scanner.nextLine();

        var json = api.getData(ADDRESS + serieName.replace(" ", "+") + API_KEY);

        ConvertsData conversor = new ConvertsData();
        var data = conversor.getData(json, SerieData.class);

    }
}
