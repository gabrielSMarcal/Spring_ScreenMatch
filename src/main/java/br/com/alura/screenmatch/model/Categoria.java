package br.com.alura.screenmatch.model;

public enum Categoria {

    ACAO("Action"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime");

    private String categoriaOmdb;

    Categoria(String descricao) {
        this.categoriaOmdb = descricao;
    }

    public String getCategoriaOmdb() {
        return categoriaOmdb;
    }

    public static Categoria fromString(String text) {

        for (Categoria categoria : Categoria.values()) {
            if (categoria.getCategoriaOmdb().equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}
