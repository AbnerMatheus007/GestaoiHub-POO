package persistencia;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import modelo.ItemPedido;
import modelo.Pedido;
import modelo.Produto;

public class GravadorDeDados {

    public static final String NOME_ARQUIVO_PRODUTOS = "produtos.dat";
    public static final String NOME_ARQUIVO_PEDIDOS = "pedidos.dat";
    public static final String NOME_ARQUIVO_SALDO = "saldo.dat";

    //PRODUTOS

    public void salvarProdutos(Collection<Produto> produtos) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO_PRODUTOS))) {
            out.writeObject(new ArrayList<>(produtos));
        }
    }

    @SuppressWarnings("unchecked")
    public List<Produto> recuperarProdutos() throws IOException {
        File arquivo = new File(NOME_ARQUIVO_PRODUTOS);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Produto>) in.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Classe Produto não encontrada ao recuperar objetos.", e);
        }
    }

    // PEDIDOS

    public void salvarPedidos(Collection<Pedido> pedidos) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO_PEDIDOS))) {
            out.writeObject(new ArrayList<>(pedidos));
        }
    }

    @SuppressWarnings("unchecked")
    public List<Pedido> recuperarPedidos(Object clientes, Object vendedores, Map<String, Produto> produtos) throws IOException {
        File arquivo = new File(NOME_ARQUIVO_PEDIDOS);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        List<Pedido> pedidos;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(arquivo))) {
            pedidos = (List<Pedido>) in.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Classe Pedido não encontrada ao recuperar objetos.", e);
        }

        for (Pedido pedido : pedidos) {
            for (ItemPedido item : pedido.getItens()) {
                Produto produtoAtual = produtos.get(item.getProduto().getNome().toLowerCase());
                if (produtoAtual != null) {
                    item.setProduto(produtoAtual);
                }
            }
        }
        return pedidos;
    }

    //SALDO

    public void salvarSaldo(double saldo) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO_SALDO))) {
            out.writeObject(Double.valueOf(saldo));
        }
    }

    public double recuperarSaldo() throws IOException {
        File arquivo = new File(NOME_ARQUIVO_SALDO);
        if (!arquivo.exists()) {
            return 0.0;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (Double) in.readObject();
        } catch (EOFException e) {
            return 0.0;
        } catch (ClassNotFoundException e) {
            throw new IOException("Classe Double não encontrada ao recuperar o saldo.", e);
        }
    }
}