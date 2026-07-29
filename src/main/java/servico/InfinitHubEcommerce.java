package servico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import excecoes.EstoqueInsuficienteException;
import excecoes.PedidoNaoEncontradoException;
import excecoes.ProdutoNaoEncontradoException;
import modelo.CategoriaProduto;
import modelo.ItemPedido;
import modelo.Pedido;
import modelo.Produto;
import modelo.StatusPedido;
import persistencia.GravadorDeDados;

public class InfinitHubEcommerce implements SistemaInfinitHub {

    private Map<String, Produto> produtos = new HashMap<>();
    private Map<String, Pedido> pedidos = new HashMap<>();
    private double saldoAtual = 0.0;

    private GravadorDeDados gravadorDeDados = new GravadorDeDados();

    @Override
    public double calcularLucroTotal() {
        return pedidos.values().stream()
                .flatMap(p -> p.getItens().stream())
                .mapToDouble(item -> (item.getProduto().getPrecoVenda() - item.getProduto().getPrecoCusto()) * item.getQuantidade())
                .sum();
    }

    @Override
    public void cadastrarProduto(Produto produto) {
        produtos.put(produto.getNome().toLowerCase(), produto);
        this.saldoAtual -= (produto.getPrecoCusto() * produto.getQuantidadeEmEstoque());
    }

    @Override
    public void removerProduto(String nome) throws ProdutoNaoEncontradoException {
        if (!produtos.containsKey(nome.toLowerCase())) {
            throw new ProdutoNaoEncontradoException("Produto não encontrado: " + nome);
        }
        produtos.remove(nome.toLowerCase());
    }

    @Override
    public Produto pesquisarProdutoPorNome(String nome) throws ProdutoNaoEncontradoException {
        Produto p = produtos.get(nome.toLowerCase());
        if (p == null) {
            throw new ProdutoNaoEncontradoException("Produto não encontrado: " + nome);
        }
        return p;
    }

    @Override
    public List<Produto> listarTodosProdutos() {
        return new ArrayList<>(produtos.values());
    }

    @Override
    public List<Produto> pesquisarProdutosPorCategoria(CategoriaProduto categoria) {
        return produtos.values().stream()
                .filter(p -> p.getCategoria() == categoria)
                .collect(Collectors.toList());
    }

    @Override
    public List<Produto> listarProdutosComEstoqueDisponivel() {
        return produtos.values().stream()
                .filter(p -> p.getQuantidadeEmEstoque() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public void reporEstoque(String nome, int quantidade, double custoUnitario) throws ProdutoNaoEncontradoException {
        Produto produto = pesquisarProdutoPorNome(nome);
        produto.reporEstoque(quantidade);
        this.saldoAtual -= (quantidade * custoUnitario);
    }

    @Override
    public void cadastrarPedido(Pedido pedido) throws EstoqueInsuficienteException, ProdutoNaoEncontradoException {
        // Valida estoque
        for (ItemPedido item : pedido.getItens()) {
            Produto p = pesquisarProdutoPorNome(item.getProduto().getNome());
            if (!p.possuiEstoqueDisponivel(item.getQuantidade())) {
                throw new EstoqueInsuficienteException("Estoque insuficiente para: " + p.getNome());
            }
        }

        // Processa baixa e saldo
        for (ItemPedido item : pedido.getItens()) {
            Produto p = produtos.get(item.getProduto().getNome().toLowerCase());
            p.baixarEstoque(item.getQuantidade());
        }
        
        this.saldoAtual += pedido.getValorTotal();
        pedidos.put(pedido.getCodigo(), pedido);
    }

    @Override
    public void removerPedido(String codigo) throws PedidoNaoEncontradoException {
        Pedido p = pedidos.get(codigo);
        if (p == null) {
            throw new PedidoNaoEncontradoException("Pedido não encontrado: " + codigo);
        }
        
        this.saldoAtual -= p.getValorTotal();
        for (ItemPedido item : p.getItens()) {
            Produto prod = produtos.get(item.getProduto().getNome().toLowerCase());
            if (prod != null) prod.reporEstoque(item.getQuantidade());
        }
        p.setStatus(StatusPedido.CANCELADO);
        pedidos.remove(codigo);
    }

    @Override
    public List<Pedido> listarPedidosPorStatus(StatusPedido status) {
        return pedidos.values().stream()
                .filter(p -> p.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public double getSaldoAtual() {
        return saldoAtual;
    }

    @Override
    public void setSaldoAtual(double saldo) {
        this.saldoAtual = saldo;
    }

    @Override
    public double calcularFaturamentoTotal() {
        return pedidos.values().stream()
                .mapToDouble(Pedido::getValorTotal)
                .sum();
    }

    @Override
    public void salvarDados() throws IOException {
        gravadorDeDados.salvarProdutos(produtos.values());
        gravadorDeDados.salvarPedidos(pedidos.values());
        gravadorDeDados.salvarSaldo(saldoAtual);
    }

    @Override
    public void recuperarDados() throws IOException {
        Collection<Produto> colecaoProdutos = gravadorDeDados.recuperarProdutos();
        produtos = new HashMap<>();
        for (Produto p : colecaoProdutos) {
            produtos.put(p.getNome().toLowerCase(), p);
        }

        Collection<Pedido> colecaoPedidos = gravadorDeDados.recuperarPedidos(null, null, produtos);
        pedidos = new HashMap<>();
        for (Pedido pedido : colecaoPedidos) {
            pedidos.put(pedido.getCodigo(), pedido);
        }
        
        this.saldoAtual = gravadorDeDados.recuperarSaldo();

    }
}
