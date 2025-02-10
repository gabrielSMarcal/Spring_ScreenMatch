package br.com.alura.screenmatch.service;

public interface IConverterDados {

    <T> T getDados(String json, Class<T> classe);
}
