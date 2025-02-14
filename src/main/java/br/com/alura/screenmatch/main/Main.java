package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class Main {

    private Scanner leitura = new Scanner(System.in);

    private ConsumoApi api = new ConsumoApi();
    private ConverterDados conversor = new ConverterDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=987702fa";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();

    public Main(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void menu() {

        var opcao = -1;

        while (opcao != 0) {

            var menu = """
                                
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
                4 - Buscar série por título
                5 - Mostrar 5 melhores séries
                6 - Buscar séries por ator
                7 - Buscar séries por gênero
                8 - Filtrar séries
                9 - Buscar episódio por trecho
                
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
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesProcuradas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    top5Series();
                    break;
                case 6:
                    buscarSeriePorAtor();
                    break;
                case 7:
                    buscarSeriePorGenero();
                    break;
                case 8:
                    filtrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
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

        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);

        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {

        System.out.println("Digite o nome da série para busca: ");

        var nomeSerie = leitura.nextLine();
        var json = api.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dado = conversor.getDados(json, DadosSerie.class);
        return dado;
    }

    private void buscarEpisodioPorSerie(){

        listarSeriesProcuradas();
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {

                var json = api.obterDados(ENDERECO + serieEncontrada.getTitulo()
                        .replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.getDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numeroTemporada(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void listarSeriesProcuradas() {

        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {

        System.out.println("Digite o nome da série para busca: ");

        var nomeSerie = leitura.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        serieBuscada.ifPresentOrElse(
                s -> System.out.println("Série encontrada : " + s.getTitulo()),
                () -> System.out.println("Série não encontrada")
        );
    }

    private void top5Series() {

        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        AtomicInteger counter = new AtomicInteger(1); // Contador para mostrar a posição das séries

        seriesTop.forEach(s -> {
            System.out.println(counter.getAndIncrement() + "ª Série: " + s.getTitulo() + " | Avaliação: " + s.getAvaliacao());
        });
    }

    private void buscarSeriePorAtor() {

        System.out.println("Digite o nome do ator para busca: ");
        var nomeAtor = leitura.nextLine();

        System.out.println("Avaliação mínima: ");
        var notaSerie = leitura.nextDouble();

        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual
                (nomeAtor, notaSerie);

        if (seriesEncontradas.isEmpty()) {
            System.out.println("Nenhuma série encontrada para o ator informado");
        } else {
            System.out.println
                    ("Séries que o ator " + nomeAtor + " participa com avaliação maior ou igual a " + notaSerie);
            seriesEncontradas.forEach(s ->
                    System.out.println("Série: " + s.getTitulo() + " | Avaliação: " + s.getAvaliacao()));
        }
    }

    private void buscarSeriePorGenero() {

        System.out.println("Digite o gênero/categoria da série para busca: ");
        var nomeGenero = leitura.nextLine();

        Categoria categoria = Categoria.fromStringPortugues(nomeGenero);
        List<Serie> seriePorCategoria = repositorio.findByGenero(categoria);

        System.out.println("Séries do gênero " + nomeGenero);
        seriePorCategoria.forEach(s ->
                System.out.println("Série: " + s.getTitulo()));
    }

    private void filtrarSeriesPorTemporadaEAvaliacao() {

        System.out.println("Filtrar séries de até quantas temporadas? ");
        var totalTemporadas = leitura.nextInt();

        System.out.println("Com avaliação a partir de que valor? ");
        var avaliacao = leitura.nextDouble();

        List<Serie> filtroSeries = repositorio
                .seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("Séries filtradas: ");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  | Avaliação: " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho() {

        System.out.println("Digite o trecho no nome do episódio para busca: ");
        var trecho = leitura.nextLine();

        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrech(trecho);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s | Temporada %s | Episódio %s: %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));
    }
}
