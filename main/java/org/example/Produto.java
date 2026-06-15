package org.example;

public class Produto {
    private int id;
    private String nome;
    private String categoria;
    private double preco;
    private int quantidade;

    public Produto(int id, String nome, String categoria, double preco, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void diminuirEstoque(int quantidadeVendida) {

        if (quantidadeVendida <= 0) {
            throw new IllegalArgumentException("A quantidade vendida deve ser maior que zero.");
        }

        if (quantidadeVendida > quantidade) {
            throw new IllegalArgumentException("Quantidade vendida maior que o estoque disponível.");
        }

        quantidade -= quantidadeVendida;
    }
}