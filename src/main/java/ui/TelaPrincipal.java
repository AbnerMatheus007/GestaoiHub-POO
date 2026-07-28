package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
 * Interface gráfica (Swing) do sistema de gestão da loja online Infinit Hub.
 * Apresenta uma barra de menu com as funcionalidades do sistema.
 */
public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private SistemaInfinitHub sistema = new InfinitHubEcommerce();
    private JTextArea areaSaida = new JTextArea();

    public TelaPrincipal() {
        super("Infinit Hub - Gestão da Loja");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);

        areaSaida.setEditable(false);
        add(new JScrollPane(areaSaida), BorderLayout.CENTER);

        setJMenuBar(criarBarraDeMenu());
    }

    private JMenuBar criarBarraDeMenu() {
        JMenuBar menuBar = new JMenuBar();

        // MENU PRODUTO
        JMenu menuProduto = new JMenu("Produto");
        JMenuItem itemCadastrarProduto = new JMenuItem("Cadastrar");
        JMenuItem itemPesquisarProdutoNome = new JMenuItem("Pesquisar por nome");
        JMenuItem itemPesquisarProdutoCategoria = new JMenuItem("Pesquisar por categoria");
        JMenuItem itemListarEstoque = new JMenuItem("Listar com estoque disponível");
        JMenuItem itemRemoverProduto = new JMenuItem("Remover");
        itemCadastrarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarProduto();
            }
        });
        itemPesquisarProdutoNome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarProdutosPorNome();
            }
        });
        itemPesquisarProdutoCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarProdutosPorCategoria();
            }
        });
        itemListarEstoque.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarProdutosComEstoqueDisponivel();
            }
        });
        itemRemoverProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerProduto();
            }
        });
        menuProduto.add(itemCadastrarProduto);
        menuProduto.add(itemPesquisarProdutoNome);
        menuProduto.add(itemPesquisarProdutoCategoria);
        menuProduto.add(itemListarEstoque);
        menuProduto.add(itemRemoverProduto);

        // MENU CLIENTE
        JMenu menuCliente = new JMenu("Cliente");
        JMenuItem itemCadastrarCliente = new JMenuItem("Cadastrar");
        JMenuItem itemPesquisarCliente = new JMenuItem("Pesquisar por nome");
        JMenuItem itemRemoverCliente = new JMenuItem("Remover");
        itemCadastrarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarCliente();
            }
        });
        itemPesquisarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarClientesPorNome();
            }
        });
        itemRemoverCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerCliente();
            }
        });
        menuCliente.add(itemCadastrarCliente);
        menuCliente.add(itemPesquisarCliente);
        menuCliente.add(itemRemoverCliente);

        // MENU VENDEDOR
        JMenu menuVendedor = new JMenu("Vendedor");
        JMenuItem itemCadastrarVendedor = new JMenuItem("Cadastrar");
        JMenuItem itemPesquisarVendedor = new JMenuItem("Pesquisar por nome");
        JMenuItem itemRemoverVendedor = new JMenuItem("Remover");
        itemCadastrarVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarVendedor();
            }
        });
        itemPesquisarVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarVendedorPorNome();
            }
        });
        itemRemoverVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerVendedor();
            }
        });
        menuVendedor.add(itemCadastrarVendedor);
        menuVendedor.add(itemPesquisarVendedor);
        menuVendedor.add(itemRemoverVendedor);

        // MENU PEDIDO
        JMenu menuPedido = new JMenu("Pedido");
        JMenuItem itemCadastrarPedido = new JMenuItem("Cadastrar (1 item)");
        JMenuItem itemPesquisarPedidosCliente = new JMenuItem("Pesquisar por cliente");
        JMenuItem itemListarPedidosVendedor = new JMenuItem("Listar por vendedor");
        JMenuItem itemListarPedidosStatus = new JMenuItem("Listar por status");
        JMenuItem itemTotalVendedor = new JMenuItem("Total vendido por vendedor");
        JMenuItem itemRemoverPedido = new JMenuItem("Remover/Cancelar");
        itemCadastrarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarPedido();
            }
        });
        itemPesquisarPedidosCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarPedidosPorCliente();
            }
        });
        itemListarPedidosVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarPedidosPorVendedor();
            }
        });
        itemListarPedidosStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarPedidosPorStatus();
            }
        });
        itemTotalVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularTotalVendidoPorVendedor();
            }
        });
        itemRemoverPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerPedido();
            }
        });
        menuPedido.add(itemCadastrarPedido);
        menuPedido.add(itemPesquisarPedidosCliente);
        menuPedido.add(itemListarPedidosVendedor);
        menuPedido.add(itemListarPedidosStatus);
        menuPedido.add(itemTotalVendedor);
        menuPedido.add(itemRemoverPedido);

        // MENU ARQUIVO
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSalvar = new JMenuItem("Salvar dados");
        JMenuItem itemRecuperar = new JMenuItem("Recuperar dados");
        itemSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarDados();
            }
        });
        itemRecuperar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recuperarDados();
            }
        });
        menuArquivo.add(itemSalvar);
        menuArquivo.add(itemRecuperar);

        menuBar.add(menuProduto);
        menuBar.add(menuCliente);
        menuBar.add(menuVendedor);
        menuBar.add(menuPedido);
        menuBar.add(menuArquivo);
        return menuBar;
    }

    // PRODUTO

    private void cadastrarProduto() {
        String id = JOptionPane.showInputDialog(this, "ID do produto:");
        if (id == null) return;
        String nome = JOptionPane.showInputDialog(this, "Nome:");
        String categoriaStr = JOptionPane.showInputDialog(this,
                "Categoria (PERIFERICO, ELETRONICO, COMPONENTE, ACESSORIO, AUDIO):");
        String precoStr = JOptionPane.showInputDialog(this, "Preço:");
        String estoqueStr = JOptionPane.showInputDialog(this, "Quantidade em estoque:");
        try {
            CategoriaProduto categoria = CategoriaProduto.valueOf(categoriaStr.toUpperCase());
            double preco = Double.parseDouble(precoStr);
            int estoque = Integer.parseInt(estoqueStr);
            sistema.cadastrarProduto(new Produto(id, nome, categoria, preco, estoque));
            areaSaida.append("Produto cadastrado: " + nome + "\n");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Dados inválidos: " + ex.getMessage());
        }
    }

    private void pesquisarProdutosPorNome() {
        String nome = JOptionPane.showInputDialog(this, "Nome (ou parte) do produto:");
        if (nome == null) return;
        List<Produto> encontrados = sistema.pesquisarProdutosPorNome(nome);
        areaSaida.append("Produtos encontrados: " + encontrados + "\n");
    }

    private void pesquisarProdutosPorCategoria() {
        String categoriaStr = JOptionPane.showInputDialog(this, "Categoria:");
        if (categoriaStr == null) return;
        try {
            CategoriaProduto categoria = CategoriaProduto.valueOf(categoriaStr.toUpperCase());
            List<Produto> encontrados = sistema.pesquisarProdutosPorCategoria(categoria);
            areaSaida.append("Produtos da categoria " + categoria + ": " + encontrados + "\n");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Categoria inválida.");
        }
    }

    private void listarProdutosComEstoqueDisponivel() {
        List<Produto> produtos = sistema.listarProdutosComEstoqueDisponivel();
        areaSaida.append("Produtos com estoque disponível: " + produtos + "\n");
    }

    private void removerProduto() {
        String id = JOptionPane.showInputDialog(this, "ID do produto a remover:");
        if (id == null) return;
        try {
            sistema.removerProduto(id);
            areaSaida.append("Produto removido: " + id + "\n");
        } catch (ProdutoNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // CLIENTE

    private void cadastrarCliente() {
        String id = JOptionPane.showInputDialog(this, "ID do cliente:");
        if (id == null) return;
        String nome = JOptionPane.showInputDialog(this, "Nome:");
        String cpf = JOptionPane.showInputDialog(this, "CPF:");
        String telefone = JOptionPane.showInputDialog(this, "Telefone:");
        String email = JOptionPane.showInputDialog(this, "Email:");
        String endereco = JOptionPane.showInputDialog(this, "Endereço:");
        sistema.cadastrarCliente(new Cliente(id, nome, cpf, telefone, email, endereco));
        areaSaida.append("Cliente cadastrado: " + nome + "\n");
    }

    private void pesquisarClientesPorNome() {
        String nome = JOptionPane.showInputDialog(this, "Nome (ou parte) do cliente:");
        if (nome == null) return;
        List<Cliente> encontrados = sistema.pesquisarClientesPorNome(nome);
        areaSaida.append("Clientes encontrados: " + encontrados + "\n");
    }

    private void removerCliente() {
        String id = JOptionPane.showInputDialog(this, "ID do cliente a remover:");
        if (id == null) return;
        try {
            sistema.removerCliente(id);
            areaSaida.append("Cliente removido: " + id + "\n");
        } catch (ClienteNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // VENDEDOR

    private void cadastrarVendedor() {
        String id = JOptionPane.showInputDialog(this, "ID do vendedor:");
        if (id == null) return;
        String nome = JOptionPane.showInputDialog(this, "Nome:");
        String cpf = JOptionPane.showInputDialog(this, "CPF:");
        String comissaoStr = JOptionPane.showInputDialog(this, "Percentual de comissão:");
        try {
            double comissao = Double.parseDouble(comissaoStr);
            sistema.cadastrarVendedor(new Vendedor(id, nome, cpf, comissao));
            areaSaida.append("Vendedor cadastrado: " + nome + "\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Percentual de comissão inválido.");
        }
    }

    private void pesquisarVendedorPorNome() {
        String nome = JOptionPane.showInputDialog(this, "Nome (ou parte) do vendedor:");
        if (nome == null) return;
        List<Vendedor> encontrados = sistema.pesquisarVendedorPorNome(nome);
        areaSaida.append("Vendedores encontrados: " + encontrados + "\n");
    }

    private void removerVendedor() {
        String id = JOptionPane.showInputDialog(this, "ID do vendedor a remover:");
        if (id == null) return;
        try {
            sistema.removerVendedor(id);
            areaSaida.append("Vendedor removido: " + id + "\n");
        } catch (VendedorNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // PEDIDO

    private void cadastrarPedido() {
        String id = JOptionPane.showInputDialog(this, "ID do pedido:");
        if (id == null) return;
        String clienteId = JOptionPane.showInputDialog(this, "ID do cliente:");
        String vendedorId = JOptionPane.showInputDialog(this, "ID do vendedor:");
        String produtoId = JOptionPane.showInputDialog(this, "ID do produto:");
        String quantidadeStr = JOptionPane.showInputDialog(this, "Quantidade:");
        try {
            Cliente cliente = sistema.pesquisarClientePorId(clienteId);
            Vendedor vendedor = sistema.pesquisarVendedorPorId(vendedorId);
            Produto produto = sistema.pesquisarProdutoPorId(produtoId);
            int quantidade = Integer.parseInt(quantidadeStr);

            List<ItemPedido> itens = new ArrayList<>();
            itens.add(new ItemPedido(produto, quantidade));

            Pedido pedido = new Pedido(id, cliente, vendedor, itens, LocalDate.now());
            sistema.cadastrarPedido(pedido);
            areaSaida.append("Pedido cadastrado: " + id + " (total: " + pedido.getValorTotal() + ")\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.");
        } catch (ClienteNaoEncontradoException | VendedorNaoEncontradoException
                 | ProdutoNaoEncontradoException | EstoqueInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void pesquisarPedidosPorCliente() {
        String clienteId = JOptionPane.showInputDialog(this, "ID do cliente:");
        if (clienteId == null) return;
        List<Pedido> encontrados = sistema.pesquisarPedidosPorCliente(clienteId);
        areaSaida.append("Pedidos do cliente " + clienteId + ": " + encontrados + "\n");
    }

    private void listarPedidosPorVendedor() {
        String vendedorId = JOptionPane.showInputDialog(this, "ID do vendedor:");
        if (vendedorId == null) return;
        List<Pedido> pedidos = sistema.listarPedidosPorVendedor(vendedorId);
        areaSaida.append("Pedidos do vendedor " + vendedorId + ": " + pedidos + "\n");
    }

    private void listarPedidosPorStatus() {
        String statusStr = JOptionPane.showInputDialog(this,
                "Status (AGUARDANDO_PAGAMENTO, PAGO, ENVIADO, ENTREGUE, CANCELADO):");
        if (statusStr == null) return;
        try {
            StatusPedido status = StatusPedido.valueOf(statusStr.toUpperCase());
            List<Pedido> pedidos = sistema.listarPedidosPorStatus(status);
            areaSaida.append("Pedidos com status " + status + ": " + pedidos + "\n");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Status inválido.");
        }
    }

    private void calcularTotalVendidoPorVendedor() {
        String vendedorId = JOptionPane.showInputDialog(this, "ID do vendedor:");
        if (vendedorId == null) return;
        double total = sistema.calcularTotalVendidoPorVendedor(vendedorId);
        areaSaida.append("Total vendido pelo vendedor " + vendedorId + ": " + total + "\n");
    }

    private void removerPedido() {
        String id = JOptionPane.showInputDialog(this, "ID do pedido a remover/cancelar:");
        if (id == null) return;
        try {
            sistema.removerPedido(id);
            areaSaida.append("Pedido removido/cancelado: " + id + "\n");
        } catch (PedidoNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // ARQUIVO E GRAVAÇÃO DE DADOS

    private void salvarDados() {
        try {
            sistema.salvarDados();
            areaSaida.append("Dados salvos com sucesso.\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar dados: " + ex.getMessage());
        }
    }

    private void recuperarDados() {
        try {
            sistema.recuperarDados();
            areaSaida.append("Dados recuperados com sucesso.\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao recuperar dados: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        TelaPrincipal tela = new TelaPrincipal();
        tela.setVisible(true);
    }
}
