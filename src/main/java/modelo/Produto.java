package modelo;

/**
 * Representa um produto (periférico, eletrônico, componente, acessório ou item de
 * áudio) do catálogo da Infinit Hub.
 */
public class Produto {

    private String id;
    private String nome;
    private CategoriaProduto categoria;
    private double preco;
    private int quantidadeEmEstoque;

    public Produto(String id, String nome, CategoriaProduto categoria, double preco, int quantidadeEmEstoque) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    /**
     * Verifica se há unidades suficientes em estoque para atender a quantidade pedida.
     */
    public boolean possuiEstoqueDisponivel(int quantidade) {
        return quantidadeEmEstoque >= quantidade;
    }

    /**
     * Dá baixa no estoque após a confirmação de um pedido.
     */
    public void baixarEstoque(int quantidade) {
        this.quantidadeEmEstoque -= quantidade;
    }

    /**
     * Repõe o estoque, usado por exemplo quando um pedido é cancelado/removido.
     */
    public void reporEstoque(int quantidade) {
        this.quantidadeEmEstoque += quantidade;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", categoria=" + categoria +
                ", preco=" + preco +
                ", quantidadeEmEstoque=" + quantidadeEmEstoque +
                '}';
    }
}
