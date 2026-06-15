package org.example;

import java.util.ArrayList;

public class Loja {

    private ArrayList<Produto> produtos = new ArrayList<>();
    private int proximoId = 1;

    public void cadastrarProduto(String nome, String categoria, double preco, int quantidade) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do produto não pode estar vazio.");
        }

        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("A categoria não pode estar vazia.");
        }

        if (preco <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }

        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa.");
        }

        Produto produto = new Produto(
                proximoId++,
                nome,
                categoria,
                preco,
                quantidade
        );

        produtos.add(produto);
    }

    public ArrayList<Produto> listarProdutos() {
        return produtos;
    }

    public void venderProduto(int id, int quantidadeVendida) {

        if (quantidadeVendida <= 0) {
            throw new IllegalArgumentException("A quantidade vendida deve ser maior que zero.");
        }

        for (Produto produto : produtos) {

            if (produto.getId() == id) {

                if (quantidadeVendida > produto.getQuantidade()) {
                    throw new IllegalArgumentException(
                            "Estoque insuficiente. Disponível: "
                                    + produto.getQuantidade()
                    );
                }

                produto.diminuirEstoque(quantidadeVendida);
                return;
            }
        }

        throw new IllegalArgumentException("Produto não encontrado.");
    }
}