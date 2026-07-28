package servico;

import java.io.IOException;
import java.util.List;

import excecoes.ClienteNaoEncontradoException;
import excecoes.EstoqueInsuficienteException;
import excecoes.PedidoNaoEncontradoException;
import excecoes.ProdutoNaoEncontradoException;
import excecoes.VendedorNaoEncontradoException;
import modelo.CategoriaProduto;
import modelo.Cliente;
import modelo.Pedido;
import modelo.Produto;
import modelo.StatusPedido;
import modelo.Vendedor;

/**
 * Interface (Façade) do sistema de gestão da loja online Infinit Hub.
 * Reúne as principais funcionalidades relacionadas a Produto, Cliente, Vendedor e
 * Pedido, além da persistência dos dados em arquivo.
 */
public interface SistemaInfinitHub {

    // PRODUTO
    void cadastrarProduto(Produto produto);

    void removerProduto(String id) throws ProdutoNaoEncontradoException;

    Produto pesquisarProdutoPorId(String id) throws ProdutoNaoEncontradoException;

    List<Produto> pesquisarProdutosPorNome(String nome);

    List<Produto> pesquisarProdutosPorCategoria(CategoriaProduto categoria);

    List<Produto> listarProdutosComEstoqueDisponivel();

    List<String> listarNomesDosProdutos();

    // CLIENTE
    void cadastrarCliente(Cliente cliente);

    void removerCliente(String id) throws ClienteNaoEncontradoException;

    Cliente pesquisarClientePorId(String id) throws ClienteNaoEncontradoException;

    List<Cliente> pesquisarClientesPorNome(String nome);

    // VENDEDOR
    void cadastrarVendedor(Vendedor vendedor);

    void removerVendedor(String id) throws VendedorNaoEncontradoException;

    Vendedor pesquisarVendedorPorId(String id) throws VendedorNaoEncontradoException;

    List<Vendedor> pesquisarVendedorPorNome(String nome);

    // PEDIDO
    void cadastrarPedido(Pedido pedido) throws EstoqueInsuficienteException,
            ClienteNaoEncontradoException, VendedorNaoEncontradoException, ProdutoNaoEncontradoException;

    void removerPedido(String id) throws PedidoNaoEncontradoException;

    List<Pedido> pesquisarPedidosPorCliente(String clienteId);

    List<Pedido> listarPedidosPorVendedor(String vendedorId);

    List<Pedido> listarPedidosPorStatus(StatusPedido status);

    double calcularTotalVendidoPorVendedor(String vendedorId);

    // PERSISTÊNCIA
    void salvarDados() throws IOException;

    void recuperarDados() throws IOException;
}
