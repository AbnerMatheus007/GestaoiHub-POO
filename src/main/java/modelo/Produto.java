package modelo;

import java.util.Objects;

public class Produto {

    private String nome;
    private CategoriaProduto categoria;
    private double preco;
    private int quantidadeEmEstoque;

    public Produto(String nome, CategoriaProduto categoria, double preco, int quantidadeEmEstoque) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CategoriaProduto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProduto categoria) {
        this.categoria = categoria;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    public void setQuantidadeEmEstoque(int quantidadeEmEstoque) {
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public boolean possuiEstoqueDisponivel(int quantidade) {
        return quantidadeEmEstoque >= quantidade;
    }

    public void baixarEstoque(int quantidade) {
        this.quantidadeEmEstoque -= quantidade;
    }

    public void reporEstoque(int quantidade) {
        this.quantidadeEmEstoque += quantidade;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - R$ %.2f [Estoque: %d]", nome, categoria, preco, quantidadeEmEstoque);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(nome.toLowerCase(), produto.nome.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase());
    }
}
