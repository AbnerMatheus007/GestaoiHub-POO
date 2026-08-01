package modelo;

import java.io.Serializable;

public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private Produto produto;
    private int quantidade;

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return produto.getPrecoVenda() * quantidade;
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "produto=" + produto.getNome() +
                ", quantidade=" + quantidade +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}