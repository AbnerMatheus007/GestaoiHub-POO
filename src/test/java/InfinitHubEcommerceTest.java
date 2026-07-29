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
import servico.InfinitHubEcommerce;
import servico.SistemaInfinitHub;

/**
 * Testes atualizados para a Versão Final (Sem IDs e com Preço de Custo).
 */
public class InfinitHubEcommerceTest {

    private SistemaInfinitHub sistema;

    @BeforeEach
    public void configurar() {
        sistema = new InfinitHubEcommerce();
    }

    @Test
    public void testCadastrarEPesquisarProdutoPorNome() throws ProdutoNaoEncontradoException {
        // Agora passamos: Nome, Categoria, Preço Venda, Preço Custo, Estoque
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
        sistema.cadastrarProduto(p); // Investimento inicial: 0 * 20 = 0

        sistema.reporEstoque("Fone", 2, 10.0); // Custo 2 * 10 = 20.0

        assertEquals(2, sistema.pesquisarProdutoPorNome("Fone").getQuantidadeEmEstoque());
        assertEquals(80.0, sistema.getSaldoAtual());
    }

    @Test
    public void testVendaELucro() throws ProdutoNaoEncontradoException, EstoqueInsuficienteException {
        sistema.setSaldoAtual(0.0);
        // Custo: 50.0 | Venda: 100.0 | Lucro esperado por un: 50.0
        Produto p = new Produto("Teclado", CategoriaProduto.PERIFERICO, 100.0, 50.0, 5);
        sistema.cadastrarProduto(p); // Saldo fica -250.0 (investimento nos 5 teclados)

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(p, 2));
        Pedido ped = new Pedido(itens);
        sistema.cadastrarPedido(ped);

        // Faturamento: 200.0 | Lucro: 100.0 (2 * 50.0)
        assertEquals(200.0, sistema.calcularFaturamentoTotal());
        assertEquals(100.0, sistema.calcularLucroTotal());
    }

    @Test
    public void testPersistencia() throws IOException, ProdutoNaoEncontradoException {
        // 1. Cadastramos o produto primeiro (isso vai descontar do saldo inicial que é 0)
        sistema.cadastrarProduto(new Produto("Monitor", CategoriaProduto.ELETRONICO, 900.0, 500.0, 2));

        // 2. AGORA definimos o saldo que queremos testar se será salvo corretamente
        sistema.setSaldoAtual(1000.0);
        sistema.salvarDados();

        // 3. Criamos um novo sistema para recuperar
        SistemaInfinitHub novo = new InfinitHubEcommerce();
        novo.recuperarDados();

        // Agora o saldo recuperado deve ser exatamente 1000.0
        assertEquals(1000.0, novo.getSaldoAtual());
        assertEquals("Monitor", novo.pesquisarProdutoPorNome("Monitor").getNome());
    }
}