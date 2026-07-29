package modelo;

import java.util.Objects;

public class Produto {
    private String nome;
    private CategoriaProduto categoria;
    private double precoVenda;
    private double precoCusto;
    private int quantidadeEmEstoque;

    public Produto(String nome, CategoriaProduto categoria, double precoVenda, double precoCusto, int quantidadeEmEstoque) {
        this.nome = nome;
        this.categoria = categoria;
        this.precoVenda = precoVenda;
        this.precoCusto = precoCusto;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public String getNome() { return nome; }
    public double getPrecoVenda() { return precoVenda; }
    public double getPrecoCusto() { return precoCusto; }
    public int getQuantidadeEmEstoque() { return quantidadeEmEstoque; }
    public CategoriaProduto getCategoria() { return categoria; }

    public boolean possuiEstoqueDisponivel(int quantidade) { return quantidadeEmEstoque >= quantidade; }
    public void baixarEstoque(int quantidade) { this.quantidadeEmEstoque -= quantidade; }
    public void reporEstoque(int quantidade) { this.quantidadeEmEstoque += quantidade; }
    public void setPrecoCusto(double precoCusto) { this.precoCusto = precoCusto; }

    @Override
    public String toString() {
        return String.format("%s (%s) | Venda: R$ %.2f | Custo: R$ %.2f | Est: %d",
                nome, categoria, precoVenda, precoCusto, quantidadeEmEstoque);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(nome.toLowerCase(), produto.nome.toLowerCase());
    }

    @Override
    public int hashCode() { return Objects.hash(nome.toLowerCase()); }
}
