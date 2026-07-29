package servico;

import java.io.IOException;
import java.util.List;

import excecoes.EstoqueInsuficienteException;
import excecoes.PedidoNaoEncontradoException;
import excecoes.ProdutoNaoEncontradoException;
import modelo.CategoriaProduto;
import modelo.Pedido;
import modelo.Produto;
import modelo.StatusPedido;

public interface SistemaInfinitHub {

    void cadastrarProduto(Produto produto);

    void removerProduto(String nome) throws ProdutoNaoEncontradoException;

    Produto pesquisarProdutoPorNome(String nome) throws ProdutoNaoEncontradoException;

    List<Produto> listarTodosProdutos();

    List<Produto> pesquisarProdutosPorCategoria(CategoriaProduto categoria);

    List<Produto> listarProdutosComEstoqueDisponivel();

    void reporEstoque(String nome, int quantidade, double custoUnitario) throws ProdutoNaoEncontradoException;

    void cadastrarPedido(Pedido pedido) throws EstoqueInsuficienteException, ProdutoNaoEncontradoException;

    void removerPedido(String codigo) throws PedidoNaoEncontradoException;

    List<Pedido> listarPedidosPorStatus(StatusPedido status);

    double getSaldoAtual();

    void setSaldoAtual(double saldo);

    double calcularFaturamentoTotal();

    void salvarDados() throws IOException;

    void recuperarDados() throws IOException;

    double calcularLucroTotal();

}
