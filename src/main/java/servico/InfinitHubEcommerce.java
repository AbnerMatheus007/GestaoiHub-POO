package servico;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import excecoes.ClienteNaoEncontradoException;
import excecoes.EstoqueInsuficienteException;
import excecoes.PedidoNaoEncontradoException;
import excecoes.ProdutoNaoEncontradoException;
import excecoes.VendedorNaoEncontradoException;
import modelo.CategoriaProduto;
import modelo.Cliente;
import modelo.ItemPedido;
import modelo.Pedido;
import modelo.Produto;
import modelo.StatusPedido;
import modelo.Vendedor;
import persistencia.GravadorDeDados;

/**
 * Implementação do sistema de gestão da loja online Infinit Hub.
 * Utiliza Maps para armazenar Produtos, Clientes, Vendedores e Pedidos.
 */
public class InfinitHubEcommerce implements SistemaInfinitHub {

    private Map<String, Produto> produtos = new HashMap<>();
    private Map<String, Cliente> clientes = new HashMap<>();
    private Map<String, Vendedor> vendedores = new HashMap<>();
    private Map<String, Pedido> pedidos = new HashMap<>();

    private GravadorDeDados gravadorDeDados = new GravadorDeDados();

    // PRODUTO

    @Override
    public void cadastrarProduto(Produto produto) {
        produtos.put(produto.getId(), produto);
    }

    @Override
    public void removerProduto(String id) throws ProdutoNaoEncontradoException {
        if (!produtos.containsKey(id)) {
            throw new ProdutoNaoEncontradoException("Produto não encontrado: " + id);
        }
        produtos.remove(id);
    }

    @Override
    public Produto pesquisarProdutoPorId(String id) throws ProdutoNaoEncontradoException {
        Produto produto = produtos.get(id);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException("Produto não encontrado: " + id);
        }
        return produto;
    }

    @Override
    public List<Produto> pesquisarProdutosPorNome(String nome) {
        return produtos.values().stream()
                .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
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
    public List<String> listarNomesDosProdutos() {
        return produtos.values().stream()
                .map(Produto::getNome)
                .collect(Collectors.toList());
    }

    // CLIENTE

    @Override
    public void cadastrarCliente(Cliente cliente) {
        clientes.put(cliente.getId(), cliente);
    }

    @Override
    public void removerCliente(String id) throws ClienteNaoEncontradoException {
        if (!clientes.containsKey(id)) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado: " + id);
        }
        clientes.remove(id);
    }

    @Override
    public Cliente pesquisarClientePorId(String id) throws ClienteNaoEncontradoException {
        Cliente cliente = clientes.get(id);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado: " + id);
        }
        return cliente;
    }

    @Override
    public List<Cliente> pesquisarClientesPorNome(String nome) {
        return clientes.values().stream()
                .filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    // VENDEDOR

    @Override
    public void cadastrarVendedor(Vendedor vendedor) {
        vendedores.put(vendedor.getId(), vendedor);
    }

    @Override
    public void removerVendedor(String id) throws VendedorNaoEncontradoException {
        if (!vendedores.containsKey(id)) {
            throw new VendedorNaoEncontradoException("Vendedor não encontrado: " + id);
        }
        vendedores.remove(id);
    }

    @Override
    public Vendedor pesquisarVendedorPorId(String id) throws VendedorNaoEncontradoException {
        Vendedor vendedor = vendedores.get(id);
        if (vendedor == null) {
            throw new VendedorNaoEncontradoException("Vendedor não encontrado: " + id);
        }
        return vendedor;
    }

    @Override
    public List<Vendedor> pesquisarVendedorPorNome(String nome) {
        return vendedores.values().stream()
                .filter(v -> v.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    // PEDIDO

    @Override
    public void cadastrarPedido(Pedido pedido) throws EstoqueInsuficienteException,
            ClienteNaoEncontradoException, VendedorNaoEncontradoException, ProdutoNaoEncontradoException {

        // valida cliente e vendedor
        pesquisarClientePorId(pedido.getCliente().getId());
        pesquisarVendedorPorId(pedido.getVendedor().getId());

        // valida cada item: produto existe e possui estoque suficiente
        for (ItemPedido item : pedido.getItens()) {
            Produto produto = pesquisarProdutoPorId(item.getProduto().getId());
            if (!produto.possuiEstoqueDisponivel(item.getQuantidade())) {
                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para o produto: " + produto.getNome());
            }
        }

        // tudo certo: dá baixa no estoque de cada produto e grava o pedido
        for (ItemPedido item : pedido.getItens()) {
            Produto produto = produtos.get(item.getProduto().getId());
            produto.baixarEstoque(item.getQuantidade());
        }
        pedido.setStatus(StatusPedido.PAGO);
        pedidos.put(pedido.getId(), pedido);
    }

    @Override
    public void removerPedido(String id) throws PedidoNaoEncontradoException {
        Pedido pedido = pedidos.get(id);
        if (pedido == null) {
            throw new PedidoNaoEncontradoException("Pedido não encontrado: " + id);
        }
        // devolve as unidades ao estoque antes de remover/cancelar o pedido
        for (ItemPedido item : pedido.getItens()) {
            Produto produto = produtos.get(item.getProduto().getId());
            if (produto != null) {
                produto.reporEstoque(item.getQuantidade());
            }
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        pedidos.remove(id);
    }

    @Override
    public List<Pedido> pesquisarPedidosPorCliente(String clienteId) {
        return pedidos.values().stream()
                .filter(p -> p.getCliente().getId().equals(clienteId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> listarPedidosPorVendedor(String vendedorId) {
        return pedidos.values().stream()
                .filter(p -> p.getVendedor().getId().equals(vendedorId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> listarPedidosPorStatus(StatusPedido status) {
        return pedidos.values().stream()
                .filter(p -> p.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public double calcularTotalVendidoPorVendedor(String vendedorId) {
        return pedidos.values().stream()
                .filter(p -> p.getVendedor().getId().equals(vendedorId))
                .mapToDouble(Pedido::getValorTotal)
                .sum();
    }

    // PERSISTÊNCIA

    @Override
    public void salvarDados() throws IOException {
        gravadorDeDados.salvarProdutos(produtos.values());
        gravadorDeDados.salvarClientes(clientes.values());
        gravadorDeDados.salvarVendedores(vendedores.values());
        gravadorDeDados.salvarPedidos(pedidos.values());
    }

    @Override
    public void recuperarDados() throws IOException {
        Collection<Produto> colecaoProdutos = gravadorDeDados.recuperarProdutos();
        produtos = new HashMap<>();
        for (Produto p : colecaoProdutos) {
            produtos.put(p.getId(), p);
        }

        Collection<Cliente> colecaoClientes = gravadorDeDados.recuperarClientes();
        clientes = new HashMap<>();
        for (Cliente c : colecaoClientes) {
            clientes.put(c.getId(), c);
        }

        Collection<Vendedor> colecaoVendedores = gravadorDeDados.recuperarVendedores();
        vendedores = new HashMap<>();
        for (Vendedor v : colecaoVendedores) {
            vendedores.put(v.getId(), v);
        }

        Collection<Pedido> colecaoPedidos = gravadorDeDados.recuperarPedidos(clientes, vendedores, produtos);
        pedidos = new HashMap<>();
        for (Pedido pedido : colecaoPedidos) {
            pedidos.put(pedido.getId(), pedido);
        }
    }
}
