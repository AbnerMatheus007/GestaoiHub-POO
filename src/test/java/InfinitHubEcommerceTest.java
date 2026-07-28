import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
import servico.InfinitHubEcommerce;
import servico.SistemaInfinitHub;

/**
 * Classe de teste automático da Infinit Hub. Exercita cadastro, remoção e
 * pesquisa das principais funcionalidades do sistema, chamando os métodos da
 * classe que implementa a interface SistemaInfinitHub.
 */
public class InfinitHubEcommerceTest {

    private SistemaInfinitHub sistema;

    @BeforeEach
    public void configurar() {
        sistema = new InfinitHubEcommerce();
    }

    @Test
    public void testCadastrarEPesquisarProduto() throws ProdutoNaoEncontradoException {
        Produto produto = new Produto("P1", "Mouse Gamer RGB", CategoriaProduto.PERIFERICO, 150.0, 20);
        sistema.cadastrarProduto(produto);

        Produto encontrado = sistema.pesquisarProdutoPorId("P1");
        assertEquals("Mouse Gamer RGB", encontrado.getNome());

        List<Produto> porNome = sistema.pesquisarProdutosPorNome("mouse");
        assertEquals(1, porNome.size());

        List<Produto> porCategoria = sistema.pesquisarProdutosPorCategoria(CategoriaProduto.PERIFERICO);
        assertEquals(1, porCategoria.size());

        List<Produto> comEstoque = sistema.listarProdutosComEstoqueDisponivel();
        assertEquals(1, comEstoque.size());

        List<String> nomes = sistema.listarNomesDosProdutos();
        assertTrue(nomes.contains("Mouse Gamer RGB"));
    }

    @Test
    public void testRemoverProdutoInexistenteLancaExcecao() {
        assertThrows(ProdutoNaoEncontradoException.class, () -> sistema.removerProduto("INEXISTENTE"));
    }

    @Test
    public void testCadastrarEPesquisarCliente() throws ClienteNaoEncontradoException {
        Cliente cliente = new Cliente("C1", "Luiza Ferreira", "222.222.222-22", "83999990000",
                "luiza@email.com", "Rua das Flores, 100");
        sistema.cadastrarCliente(cliente);

        Cliente encontrado = sistema.pesquisarClientePorId("C1");
        assertEquals("Luiza Ferreira", encontrado.getNome());

        List<Cliente> porNome = sistema.pesquisarClientesPorNome("luiza");
        assertEquals(1, porNome.size());

        sistema.removerCliente("C1");
        assertThrows(ClienteNaoEncontradoException.class, () -> sistema.pesquisarClientePorId("C1"));
    }

    @Test
    public void testCadastrarEPesquisarVendedor() throws VendedorNaoEncontradoException {
        Vendedor vendedor = new Vendedor("V1", "Abner Matheus", "111.111.111-11", 5.0);
        sistema.cadastrarVendedor(vendedor);

        Vendedor encontrado = sistema.pesquisarVendedorPorId("V1");
        assertEquals("Abner Matheus", encontrado.getNome());

        List<Vendedor> porNome = sistema.pesquisarVendedorPorNome("abner");
        assertEquals(1, porNome.size());

        sistema.removerVendedor("V1");
        assertThrows(VendedorNaoEncontradoException.class, () -> sistema.pesquisarVendedorPorId("V1"));
    }

    @Test
    public void testCadastrarPedidoComSucessoEBaixaEstoque() throws ClienteNaoEncontradoException,
            VendedorNaoEncontradoException, ProdutoNaoEncontradoException, EstoqueInsuficienteException {

        Vendedor vendedor = new Vendedor("V1", "Abner Matheus", "111.111.111-11", 5.0);
        Cliente cliente = new Cliente("C1", "Luiza Ferreira", "222.222.222-22", "83999990000",
                "luiza@email.com", "Rua das Flores, 100");
        Produto produto = new Produto("P1", "Teclado Mecânico", CategoriaProduto.PERIFERICO, 250.0, 10);
        sistema.cadastrarVendedor(vendedor);
        sistema.cadastrarCliente(cliente);
        sistema.cadastrarProduto(produto);

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 2));
        Pedido pedido = new Pedido("PD1", cliente, vendedor, itens, LocalDate.now());

        sistema.cadastrarPedido(pedido);

        assertEquals(500.0, pedido.getValorTotal());
        assertEquals(StatusPedido.PAGO, pedido.getStatus());
        assertEquals(8, sistema.pesquisarProdutoPorId("P1").getQuantidadeEmEstoque());

        List<Pedido> pedidosDoCliente = sistema.pesquisarPedidosPorCliente("C1");
        assertEquals(1, pedidosDoCliente.size());

        double total = sistema.calcularTotalVendidoPorVendedor("V1");
        assertEquals(500.0, total);
    }

    @Test
    public void testCadastrarPedidoComEstoqueInsuficienteLancaExcecao() throws ClienteNaoEncontradoException,
            VendedorNaoEncontradoException, ProdutoNaoEncontradoException {

        Vendedor vendedor = new Vendedor("V1", "Abner Matheus", "111.111.111-11", 5.0);
        Cliente cliente = new Cliente("C1", "Luiza Ferreira", "222.222.222-22", "83999990000",
                "luiza@email.com", "Rua das Flores, 100");
        Produto produto = new Produto("P1", "Placa de Vídeo", CategoriaProduto.COMPONENTE, 3000.0, 1);
        sistema.cadastrarVendedor(vendedor);
        sistema.cadastrarCliente(cliente);
        sistema.cadastrarProduto(produto);

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 5));
        Pedido pedido = new Pedido("PD1", cliente, vendedor, itens, LocalDate.now());

        assertThrows(EstoqueInsuficienteException.class, () -> sistema.cadastrarPedido(pedido));
    }

    @Test
    public void testRemoverPedidoDevolveEstoque() throws ClienteNaoEncontradoException,
            VendedorNaoEncontradoException, ProdutoNaoEncontradoException, EstoqueInsuficienteException,
            PedidoNaoEncontradoException {

        Vendedor vendedor = new Vendedor("V1", "Abner Matheus", "111.111.111-11", 5.0);
        Cliente cliente = new Cliente("C1", "Luiza Ferreira", "222.222.222-22", "83999990000",
                "luiza@email.com", "Rua das Flores, 100");
        Produto produto = new Produto("P1", "Headset Gamer", CategoriaProduto.AUDIO, 300.0, 5);
        sistema.cadastrarVendedor(vendedor);
        sistema.cadastrarCliente(cliente);
        sistema.cadastrarProduto(produto);

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 2));
        Pedido pedido = new Pedido("PD1", cliente, vendedor, itens, LocalDate.now());
        sistema.cadastrarPedido(pedido);

        sistema.removerPedido("PD1");

        assertTrue(sistema.pesquisarPedidosPorCliente("C1").isEmpty());
        assertEquals(5, sistema.pesquisarProdutoPorId("P1").getQuantidadeEmEstoque());
        assertThrows(PedidoNaoEncontradoException.class, () -> sistema.removerPedido("PD1"));
    }

    @Test
    public void testListarPedidosPorVendedorEPorStatus() throws ClienteNaoEncontradoException,
            VendedorNaoEncontradoException, ProdutoNaoEncontradoException, EstoqueInsuficienteException {

        Vendedor vendedor = new Vendedor("V1", "Abner Matheus", "111.111.111-11", 5.0);
        Cliente cliente = new Cliente("C1", "Luiza Ferreira", "222.222.222-22", "83999990000",
                "luiza@email.com", "Rua das Flores, 100");
        Produto produtoA = new Produto("P1", "Webcam Full HD", CategoriaProduto.PERIFERICO, 200.0, 10);
        Produto produtoB = new Produto("P2", "Caixa de Som Bluetooth", CategoriaProduto.AUDIO, 180.0, 10);
        sistema.cadastrarVendedor(vendedor);
        sistema.cadastrarCliente(cliente);
        sistema.cadastrarProduto(produtoA);
        sistema.cadastrarProduto(produtoB);

        List<ItemPedido> itens1 = new ArrayList<>();
        itens1.add(new ItemPedido(produtoA, 1));
        sistema.cadastrarPedido(new Pedido("PD1", cliente, vendedor, itens1, LocalDate.now()));

        List<ItemPedido> itens2 = new ArrayList<>();
        itens2.add(new ItemPedido(produtoB, 1));
        sistema.cadastrarPedido(new Pedido("PD2", cliente, vendedor, itens2, LocalDate.now()));

        List<Pedido> pedidosDoVendedor = sistema.listarPedidosPorVendedor("V1");
        assertEquals(2, pedidosDoVendedor.size());

        List<Pedido> pedidosPagos = sistema.listarPedidosPorStatus(StatusPedido.PAGO);
        assertEquals(2, pedidosPagos.size());

        List<Pedido> pedidosCancelados = sistema.listarPedidosPorStatus(StatusPedido.CANCELADO);
        assertTrue(pedidosCancelados.isEmpty());
    }

    @Test
    public void testSalvarERecuperarDados() throws ClienteNaoEncontradoException,
            VendedorNaoEncontradoException, ProdutoNaoEncontradoException, EstoqueInsuficienteException,
            IOException {

        Vendedor vendedor = new Vendedor("V1", "Abner Matheus", "111.111.111-11", 5.0);
        Cliente cliente = new Cliente("C1", "Luiza Ferreira", "222.222.222-22", "83999990000",
                "luiza@email.com", "Rua das Flores, 100");
        Produto produto = new Produto("P1", "Monitor 24 polegadas", CategoriaProduto.ELETRONICO, 900.0, 4);
        sistema.cadastrarVendedor(vendedor);
        sistema.cadastrarCliente(cliente);
        sistema.cadastrarProduto(produto);

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 1));
        sistema.cadastrarPedido(new Pedido("PD1", cliente, vendedor, itens, LocalDate.now()));

        sistema.salvarDados();

        SistemaInfinitHub novoSistema = new InfinitHubEcommerce();
        novoSistema.recuperarDados();

        assertEquals("Abner Matheus", novoSistema.pesquisarVendedorPorId("V1").getNome());
        assertEquals("Luiza Ferreira", novoSistema.pesquisarClientePorId("C1").getNome());
        assertEquals("Monitor 24 polegadas", novoSistema.pesquisarProdutoPorId("P1").getNome());
        assertFalse(novoSistema.pesquisarPedidosPorCliente("C1").isEmpty());
    }
}
