package br.com.alura.screenmatch;

import br.com.alura.screenmatch.service.ApiConsumer;
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
		var json = api.obterDados("https://www.omdbapi.com/?t=gilmore+girls&Season=1&apikey=6585022c");
		System.out.println(json);

		json = api.obterDados("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);
	}
}
