package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.EpisodeData;
import br.com.alura.screenmatch.model.SerieData;
import br.com.alura.screenmatch.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
	public static void main(String[] args) {

		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var api = new ApiConsumer();
		var json = api.getData("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		System.out.println(json);
		System.out.println("-------------------------------------------------");

		ConvertsData conversor = new ConvertsData();
		var data = conversor.getData(json, SerieData.class);
		System.out.println(data);
		System.out.println("-------------------------------------------------");

		json = api.getData("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=6585022c");
		EpisodeData episode = conversor.getData(json, EpisodeData.class);
		System.out.println(episode);
		System.out.println("-------------------------------------------------");


	}
}
