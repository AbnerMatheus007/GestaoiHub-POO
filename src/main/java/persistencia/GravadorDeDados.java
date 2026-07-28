package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import modelo.CategoriaProduto;
import modelo.Cliente;
import modelo.ItemPedido;
import modelo.Pedido;
import modelo.Produto;
import modelo.StatusPedido;
import modelo.Vendedor;

/**
 * Classe responsável pela persistência de dados em arquivo de texto (.txt),
 * gravando e recuperando os objetos do sistema. Cada linha do arquivo representa
 * um objeto, com os campos separados por ";". Por ser texto puro, o arquivo pode
 * ser aberto e conferido em qualquer editor de texto comum.
 *
 * Todos os métodos usam try-with-resources, garantindo o fechamento automático
 * dos arquivos mesmo em caso de exceção.
 */
public class GravadorDeDados {

    public static final String NOME_ARQUIVO_PRODUTOS = "produtos.txt";
    public static final String NOME_ARQUIVO_CLIENTES = "clientes.txt";
    public static final String NOME_ARQUIVO_VENDEDORES = "vendedores.txt";
    public static final String NOME_ARQUIVO_PEDIDOS = "pedidos.txt";

    private static final String SEPARADOR = ";";
    private static final String SEPARADOR_ITENS = ",";
    private static final String SEPARADOR_ITEM_CAMPO = ":";

    // PRODUTO

    public void salvarProdutos(Collection<Produto> produtos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO_PRODUTOS))) {
            for (Produto p : produtos) {
                writer.write(p.getId() + SEPARADOR + p.getNome() + SEPARADOR + p.getCategoria()
                        + SEPARADOR + p.getPreco() + SEPARADOR + p.getQuantidadeEmEstoque());
                writer.newLine();
            }
        }
    }

    public List<Produto> recuperarProdutos() throws IOException {
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO_PRODUTOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] campos = linha.split(SEPARADOR);
                Produto p = new Produto(campos[0], campos[1], CategoriaProduto.valueOf(campos[2]),
                        Double.parseDouble(campos[3]), Integer.parseInt(campos[4]));
                produtos.add(p);
            }
        }
        return produtos;
    }

    // CLIENTE

    public void salvarClientes(Collection<Cliente> clientes) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO_CLIENTES))) {
            for (Cliente c : clientes) {
                writer.write(c.getId() + SEPARADOR + c.getNome() + SEPARADOR + c.getCpf() + SEPARADOR
                        + c.getTelefone() + SEPARADOR + c.getEmail() + SEPARADOR + c.getEndereco());
                writer.newLine();
            }
        }
    }

    public List<Cliente> recuperarClientes() throws IOException {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO_CLIENTES))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] campos = linha.split(SEPARADOR);
                Cliente c = new Cliente(campos[0], campos[1], campos[2], campos[3], campos[4], campos[5]);
                clientes.add(c);
            }
        }
        return clientes;
    }

    // VENDEDOR

    public void salvarVendedores(Collection<Vendedor> vendedores) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO_VENDEDORES))) {
            for (Vendedor v : vendedores) {
                writer.write(v.getId() + SEPARADOR + v.getNome() + SEPARADOR
                        + v.getCpf() + SEPARADOR + v.getPercentualComissao());
                writer.newLine();
            }
        }
    }

    public List<Vendedor> recuperarVendedores() throws IOException {
        List<Vendedor> vendedores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO_VENDEDORES))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] campos = linha.split(SEPARADOR);
                Vendedor v = new Vendedor(campos[0], campos[1], campos[2], Double.parseDouble(campos[3]));
                vendedores.add(v);
            }
        }
        return vendedores;
    }

    // PEDIDO

    /**
     * Grava os pedidos. Os itens de cada pedido são serializados em uma única coluna
     * no formato "idProduto:quantidade,idProduto:quantidade,...".
     */
    public void salvarPedidos(Collection<Pedido> pedidos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO_PEDIDOS))) {
            for (Pedido pedido : pedidos) {
                String itensSerializados = serializarItens(pedido.getItens());
                writer.write(pedido.getId() + SEPARADOR + pedido.getCliente().getId() + SEPARADOR
                        + pedido.getVendedor().getId() + SEPARADOR + itensSerializados + SEPARADOR
                        + pedido.getDataPedido() + SEPARADOR + pedido.getStatus());
                writer.newLine();
            }
        }
    }

    private String serializarItens(List<ItemPedido> itens) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < itens.size(); i++) {
            ItemPedido item = itens.get(i);
            sb.append(item.getProduto().getId()).append(SEPARADOR_ITEM_CAMPO).append(item.getQuantidade());
            if (i < itens.size() - 1) {
                sb.append(SEPARADOR_ITENS);
            }
        }
        return sb.toString();
    }

    /**
     * Recupera os pedidos relinkando cada um ao respectivo Cliente, Vendedor e
     * Produtos, a partir dos Maps já carregados previamente (o arquivo de pedidos
     * guarda apenas os ids, não os dados inteiros).
     */
    public List<Pedido> recuperarPedidos(Map<String, Cliente> clientes, Map<String, Vendedor> vendedores,
                                          Map<String, Produto> produtos) throws IOException {
        List<Pedido> pedidos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO_PEDIDOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] campos = linha.split(SEPARADOR);
                Cliente cliente = clientes.get(campos[1]);
                Vendedor vendedor = vendedores.get(campos[2]);
                List<ItemPedido> itens = desserializarItens(campos[3], produtos);
                LocalDate dataPedido = LocalDate.parse(campos[4]);
                Pedido pedido = new Pedido(campos[0], cliente, vendedor, itens, dataPedido);
                pedido.setStatus(StatusPedido.valueOf(campos[5]));
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    private List<ItemPedido> desserializarItens(String itensSerializados, Map<String, Produto> produtos) {
        List<ItemPedido> itens = new ArrayList<>();
        if (itensSerializados.isBlank()) {
            return itens;
        }
        String[] pares = itensSerializados.split(SEPARADOR_ITENS);
        for (String par : pares) {
            String[] campos = par.split(SEPARADOR_ITEM_CAMPO);
            Produto produto = produtos.get(campos[0]);
            int quantidade = Integer.parseInt(campos[1]);
            itens.add(new ItemPedido(produto, quantidade));
        }
        return itens;
    }
}
