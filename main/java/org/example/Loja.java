package org.example;

import java.util.ArrayList;

public class Loja {
    private ArrayList<Produto> produtos = new ArrayList<>();
    private int proximoId = 1;

    public void cadastrarProduto(String nome, String categoria, double preco, int quantidade) {
        produtos.add(new Produto(proximoId++, nome, categoria, preco, quantidade));
    }

    public ArrayList<Produto> listarProdutos() {
        return produtos;
    }

    public void venderProduto(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                if (produto.getQuantidade() <= 0) {
                    throw new IllegalArgumentException("Produto sem estoque.");
                }

                produto.diminuirEstoque();
                return;
            }
        }

        throw new IllegalArgumentException("Produto não encontrado.");
    }
}