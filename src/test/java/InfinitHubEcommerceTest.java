import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import excecoes.EstoqueInsuficienteException;
import excecoes.PedidoNaoEncontradoException;
import excecoes.ProdutoNaoEncontradoException;
import modelo.CategoriaProduto;
import modelo.ItemPedido;
import modelo.Pedido;
import modelo.Produto;
import modelo.StatusPedido;
import servico.InfinitHubEcommerce;
import servico.SistemaInfinitHub;

public class InfinitHubEcommerceTest {

    private SistemaInfinitHub sistema;

    @BeforeEach
    public void configurar() {
        sistema = new InfinitHubEcommerce();
    }

    @Test
    public void testCadastrarEPesquisarProdutoPorNome() throws ProdutoNaoEncontradoException {
        Produto p = new Produto("Mouse Gamer", CategoriaProduto.PERIFERICO, 150.0, 80.0, 10);
        sistema.cadastrarProduto(p);

        Produto encontrado = sistema.pesquisarProdutoPorNome("Mouse Gamer");
        assertEquals(150.0, encontrado.getPrecoVenda());
        assertEquals(80.0, encontrado.getPrecoCusto());
    }

    @Test
    public void testRemoverProdutoELancarExcecao() {
        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            sistema.removerProduto("Produto Inexistente");
        });
    }

    @Test
    public void testReporEstoqueEDescontarSaldo() throws ProdutoNaoEncontradoException {
        sistema.setSaldoAtual(100.0);
        Produto p = new Produto("Fone", CategoriaProduto.AUDIO, 50.0, 20.0, 0);
        sistema.cadastrarProduto(p);

        sistema.reporEstoque("Fone", 2, 10.0);

        assertEquals(2, sistema.pesquisarProdutoPorNome("Fone").getQuantidadeEmEstoque());
        assertEquals(80.0, sistema.getSaldoAtual());
    }

    @Test
    public void testVendaELucro() throws ProdutoNaoEncontradoException, EstoqueInsuficienteException {
        sistema.setSaldoAtual(0.0);
        Produto p = new Produto("Teclado", CategoriaProduto.PERIFERICO, 100.0, 50.0, 5);
        sistema.cadastrarProduto(p);

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(p, 2));
        Pedido ped = new Pedido(itens);
        sistema.cadastrarPedido(ped);

        assertEquals(200.0, sistema.calcularFaturamentoTotal());
        assertEquals(100.0, sistema.calcularLucroTotal());
    }

    @Test
    public void testPersistencia() throws IOException, ProdutoNaoEncontradoException {
        sistema.cadastrarProduto(new Produto("Monitor", CategoriaProduto.ELETRONICO, 900.0, 500.0, 2));

        sistema.setSaldoAtual(1000.0);
        sistema.salvarDados();

        SistemaInfinitHub novo = new InfinitHubEcommerce();
        novo.recuperarDados();

        assertEquals(1000.0, novo.getSaldoAtual());
        assertEquals("Monitor", novo.pesquisarProdutoPorNome("Monitor").getNome());
    }

    @Test
    public void testListarTodosPesquisarPorCategoriaEComEstoqueDisponivel() {
        Produto mouse = new Produto("Mouse Gamer", CategoriaProduto.PERIFERICO, 150.0, 80.0, 10);
        Produto monitor = new Produto("Monitor 24pol", CategoriaProduto.ELETRONICO, 900.0, 500.0, 0);
        sistema.cadastrarProduto(mouse);
        sistema.cadastrarProduto(monitor);

        List<Produto> todos = sistema.listarTodosProdutos();
        assertEquals(2, todos.size());

        List<Produto> perifericos = sistema.pesquisarProdutosPorCategoria(CategoriaProduto.PERIFERICO);
        assertEquals(1, perifericos.size());
        assertEquals("Mouse Gamer", perifericos.get(0).getNome());

        List<Produto> comEstoque = sistema.listarProdutosComEstoqueDisponivel();
        assertEquals(1, comEstoque.size());
        assertEquals("Mouse Gamer", comEstoque.get(0).getNome());
    }

    @Test
    public void testListarPedidosPorStatusERemoverPedido() throws ProdutoNaoEncontradoException,
            EstoqueInsuficienteException, PedidoNaoEncontradoException {

        Produto p = new Produto("Headset", CategoriaProduto.AUDIO, 300.0, 150.0, 5);
        sistema.cadastrarProduto(p);

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(p, 2));
        Pedido pedido = new Pedido(itens);
        sistema.cadastrarPedido(pedido);

        List<Pedido> pagos = sistema.listarPedidosPorStatus(StatusPedido.PAGO);
        assertEquals(1, pagos.size());
        assertEquals(3, sistema.pesquisarProdutoPorNome("Headset").getQuantidadeEmEstoque());

        sistema.removerPedido(pedido.getCodigo());

        assertTrue(sistema.listarPedidosPorStatus(StatusPedido.PAGO).isEmpty());
        assertEquals(5, sistema.pesquisarProdutoPorNome("Headset").getQuantidadeEmEstoque());
        assertThrows(PedidoNaoEncontradoException.class, () -> sistema.removerPedido(pedido.getCodigo()));
    }
}