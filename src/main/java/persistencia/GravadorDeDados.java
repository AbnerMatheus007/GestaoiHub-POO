package persistencia;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import modelo.CategoriaProduto;
import modelo.ItemPedido;
import modelo.Pedido;
import modelo.Produto;
import modelo.StatusPedido;

public class GravadorDeDados {

    public static final String NOME_ARQUIVO_PRODUTOS = "produtos.txt";
    public static final String NOME_ARQUIVO_PEDIDOS = "pedidos.txt";
    public static final String NOME_ARQUIVO_SALDO = "saldo.txt";

    private static final String SEPARADOR = ";";
    private static final String SEPARADOR_ITENS = ",";
    private static final String SEPARADOR_ITEM_CAMPO = ":";

    public void salvarProdutos(Collection<Produto> produtos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO_PRODUTOS))) {
            for (Produto p : produtos) {
                writer.write(p.getNome() + SEPARADOR + p.getCategoria() + SEPARADOR
                        + p.getPrecoVenda() + SEPARADOR + p.getPrecoCusto() + SEPARADOR + p.getQuantidadeEmEstoque());
                writer.newLine();
            }
        }
    }

    public List<Produto> recuperarProdutos() throws IOException {
        List<Produto> produtos = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO_PRODUTOS);
        if (!arquivo.exists()) return produtos;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] campos = linha.split(SEPARADOR);
                Produto p = new Produto(campos[0], CategoriaProduto.valueOf(campos[1]),
                        Double.parseDouble(campos[2]), Double.parseDouble(campos[3]), Integer.parseInt(campos[4]));
                produtos.add(p);
            }
        }
        return produtos;
    }

    public void salvarPedidos(Collection<Pedido> pedidos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO_PEDIDOS))) {
            for (Pedido p : pedidos) {
                String itensSerializados = serializarItens(p.getItens());
                writer.write(p.getCodigo() + SEPARADOR + itensSerializados + SEPARADOR
                        + p.getDataHoraPedido() + SEPARADOR + p.getStatus());
                writer.newLine();
            }
        }
    }

    private String serializarItens(List<ItemPedido> itens) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < itens.size(); i++) {
            ItemPedido item = itens.get(i);
            sb.append(item.getProduto().getNome()).append(SEPARADOR_ITEM_CAMPO).append(item.getQuantidade());
            if (i < itens.size() - 1) sb.append(SEPARADOR_ITENS);
        }
        return sb.toString();
    }

    public List<Pedido> recuperarPedidos(Object c, Object v, Map<String, Produto> produtos) throws IOException {
        List<Pedido> pedidos = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO_PEDIDOS);
        if (!arquivo.exists()) return pedidos;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] campos = linha.split(SEPARADOR);
                List<ItemPedido> itens = desserializarItens(campos[1], produtos);
                LocalDateTime dataHora = LocalDateTime.parse(campos[2]);
                Pedido p = new Pedido(campos[0], itens, dataHora, StatusPedido.valueOf(campos[3]));
                pedidos.add(p);
            }
        }
        return pedidos;
    }

    private List<ItemPedido> desserializarItens(String serializado, Map<String, Produto> produtos) {
        List<ItemPedido> itens = new ArrayList<>();
        if (serializado == null || serializado.isBlank()) return itens;

        String[] pares = serializado.split(SEPARADOR_ITENS);
        for (String par : pares) {
            String[] campos = par.split(SEPARADOR_ITEM_CAMPO);
            Produto p = produtos.get(campos[0].toLowerCase());
            if (p != null) {
                itens.add(new ItemPedido(p, Integer.parseInt(campos[1])));
            }
        }
        return itens;
    }

    public void salvarSaldo(double saldo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO_SALDO))) {
            writer.write(String.valueOf(saldo));
        }
    }

    public double recuperarSaldo() throws IOException {
        File arquivo = new File(NOME_ARQUIVO_SALDO);
        if (!arquivo.exists()) return 0.0;
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha = reader.readLine();
            return (linha != null) ? Double.parseDouble(linha) : 0.0;
        }
    }
}
