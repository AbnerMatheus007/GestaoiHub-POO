package ui;

import java.util.Map;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private SistemaInfinitHub sistema = new InfinitHubEcommerce();
    private JTextArea areaSaida = new JTextArea();
    private JLabel labelSaldo = new JLabel("Saldo: R$ 0,00");

    private final Color COLOR_PRIMARY = new Color(110, 0, 162);
    private final Color COLOR_SECONDARY = new Color(163, 0, 189);
    private final Color COLOR_BACKGROUND = new Color(236, 240, 241);
    private final Color COLOR_TEXT = new Color(76, 44, 80);

    public TelaPrincipal() {
        super("Gestão da Loja Infinit Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelLateral = new JPanel();
        painelLateral.setLayout(new GridLayout(12, 1, 5, 5));
        painelLateral.setBackground(COLOR_PRIMARY);
        painelLateral.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("@infinit_hub");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelLateral.add(titulo);

        JButton btnCadastrarProd = criarBotaoMenu("Cadastrar Produto");
        JButton btnReporEstoque = criarBotaoMenu("Repor Estoque");
        JButton btnListarProd = criarBotaoMenu("Catálogo de Produtos");
        JButton btnNovoPedido = criarBotaoMenu("NOVO PEDIDO");
        JButton btnListarPedidos = criarBotaoMenu("Histórico de Pedidos");
        JButton btnFaturamento = criarBotaoMenu("Faturamento Total");
        JButton btnAjustarSaldo = criarBotaoMenu("Ajustar Saldo");
        JButton btnSalvar = criarBotaoMenu("Salvar Dados");
        JButton btnCarregar = criarBotaoMenu("Carregar Dados");

        painelLateral.add(btnCadastrarProd);
        painelLateral.add(btnReporEstoque);
        painelLateral.add(btnListarProd);
        painelLateral.add(new JSeparator());
        painelLateral.add(btnNovoPedido);
        painelLateral.add(btnListarPedidos);
        painelLateral.add(btnFaturamento);
        painelLateral.add(new JSeparator());
        painelLateral.add(btnAjustarSaldo);
        painelLateral.add(btnSalvar);
        painelLateral.add(btnCarregar);

        add(painelLateral, BorderLayout.WEST);

        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(COLOR_BACKGROUND);
        painelCentral.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setOpaque(false);
        labelSaldo.setFont(new Font("Arial", Font.BOLD, 24));
        labelSaldo.setForeground(new Color(39, 174, 96));
        painelTopo.add(labelSaldo, BorderLayout.EAST);
        painelCentral.add(painelTopo, BorderLayout.NORTH);

        areaSaida.setEditable(false);
        areaSaida.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaSaida.setBackground(Color.WHITE);
        areaSaida.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        painelCentral.add(new JScrollPane(areaSaida), BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        btnCadastrarProd.addActionListener(e -> cadastrarProduto());
        btnReporEstoque.addActionListener(e -> reporEstoque());
        btnListarProd.addActionListener(e -> listarProdutos());
        btnNovoPedido.addActionListener(e -> realizarNovoPedido());
        btnListarPedidos.addActionListener(e -> listarPedidos());
        btnFaturamento.addActionListener(e -> exibirFaturamento());
        btnAjustarSaldo.addActionListener(e -> ajustarSaldo());
        btnSalvar.addActionListener(e -> salvarDados());
        btnCarregar.addActionListener(e -> recuperarDados());

        atualizarSaldoLabel();
    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBackground(COLOR_SECONDARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return btn;
    }

    private void atualizarSaldoLabel() {
        labelSaldo.setText(String.format("Saldo em Caixa: R$ %.2f", sistema.getSaldoAtual()));
    }

    private void cadastrarProduto() {
        String nome = JOptionPane.showInputDialog(this, "Nome do Produto:");
        if (nome == null || nome.isBlank()) return;

        String[] categorias = {"PERIFERICO", "ELETRONICO", "COMPONENTE", "ACESSORIO", "AUDIO"};
        String categoriaStr = (String) JOptionPane.showInputDialog(this, "Selecione a Categoria:", "Categoria",
                JOptionPane.QUESTION_MESSAGE, null, categorias, categorias[0]);

        String precoVendaStr = JOptionPane.showInputDialog(this, "Preço de Venda (R$):");
        String precoCustoStr = JOptionPane.showInputDialog(this, "Preço de Custo/Compra (R$):");
        String estoqueStr = JOptionPane.showInputDialog(this, "Estoque Inicial:");

        try {
            double precoVenda = Double.parseDouble(precoVendaStr);
            double precoCusto = Double.parseDouble(precoCustoStr);
            int estoque = Integer.parseInt(estoqueStr);

            sistema.cadastrarProduto(new Produto(nome, CategoriaProduto.valueOf(categoriaStr), precoVenda, precoCusto, estoque));

            areaSaida.append("Produto '" + nome + "' cadastrado e investimento descontado do saldo.\n");
            atualizarSaldoLabel();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro nos dados: " + ex.getMessage());
        }
    }

    private void reporEstoque() {
        List<Produto> todos = sistema.listarTodosProdutos();
        if (todos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum produto cadastrado.");
            return;
        }

        String[] nomes = todos.stream().map(Produto::getNome).toArray(String[]::new);
        String nomeSelecionado = (String) JOptionPane.showInputDialog(this, "Selecione o produto para repor:", 
                                   "Reposição", JOptionPane.QUESTION_MESSAGE, null, nomes, nomes[0]);
        
        if (nomeSelecionado == null) return;

        String qtdStr = JOptionPane.showInputDialog(this, "Quantidade comprada:");
        String custoStr = JOptionPane.showInputDialog(this, "Custo Unitário (será descontado do saldo):");

        try {
            int qtd = Integer.parseInt(qtdStr);
            double custo = Double.parseDouble(custoStr);
            sistema.reporEstoque(nomeSelecionado, qtd, custo);
            areaSaida.append("Estoque atualizado: " + nomeSelecionado + " (+ " + qtd + " un).\n");
            atualizarSaldoLabel();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro na reposição: " + ex.getMessage());
        }
    }

    private void listarProdutos() {
        List<Produto> produtos = sistema.listarTodosProdutos();
        areaSaida.append("\n--- CATÁLOGO DE PRODUTOS ---\n");
        if (produtos.isEmpty()) areaSaida.append("(Catálogo vazio)\n");
        for (Produto p : produtos) {
            areaSaida.append(p.toString() + "\n");
        }
    }

    private void realizarNovoPedido() {
        List<Produto> produtosOriginais = sistema.listarProdutosComEstoqueDisponivel();
        if (produtosOriginais.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há produtos com estoque disponível.");
            return;
        }

        Map<String, Integer> estoqueTemporario = new java.util.HashMap<>();
        for (Produto p : produtosOriginais) {
            estoqueTemporario.put(p.getNome(), p.getQuantidadeEmEstoque());
        }

        List<ItemPedido> itensDoPedido = new ArrayList<>();
        boolean continuarAdicionando = true;

        while (continuarAdicionando) {
            List<Produto> aindaDisponiveis = produtosOriginais.stream()
                    .filter(p -> estoqueTemporario.get(p.getNome()) > 0)
                    .collect(java.util.stream.Collectors.toList());

            if (aindaDisponiveis.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os itens do estoque foram adicionados ao carrinho!");
                break;
            }

            String[] nomes = aindaDisponiveis.stream()
                    .map(p -> p.getNome() + " (Disponível: " + estoqueTemporario.get(p.getNome()) + ")")
                    .toArray(String[]::new);

            JComboBox<String> comboProdutos = new JComboBox<>(nomes);
            JTextField txtQtd = new JTextField("1");

            JPanel painel = new JPanel(new GridLayout(0, 1));
            painel.add(new JLabel("Selecione o produto:"));
            painel.add(comboProdutos);
            painel.add(new JLabel("Quantidade:"));
            painel.add(txtQtd);

            int result = JOptionPane.showConfirmDialog(this, painel, "Adicionar ao Carrinho", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    Produto pOriginal = aindaDisponiveis.get(comboProdutos.getSelectedIndex());
                    int qtdPedida = Integer.parseInt(txtQtd.getText());
                    int qtdDisponivel = estoqueTemporario.get(pOriginal.getNome());

                    if (qtdPedida > qtdDisponivel) {
                        JOptionPane.showMessageDialog(this, "Quantidade superior ao disponível! (Máximo: " + qtdDisponivel + ")");
                        continue;
                    }

                    estoqueTemporario.put(pOriginal.getNome(), qtdDisponivel - qtdPedida);

                    itensDoPedido.add(new ItemPedido(pOriginal, qtdPedida));

                    int resposta = JOptionPane.showConfirmDialog(this, "Deseja adicionar outro produto?",
                            "Carrinho", JOptionPane.YES_NO_OPTION);
                    continuarAdicionando = (resposta == JOptionPane.YES_OPTION);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Por favor, insira um número válido.");
                }
            } else {
                itensDoPedido.clear();
                break;
            }
        }

        if (!itensDoPedido.isEmpty()) {
            try {
                Pedido pedido = new Pedido(itensDoPedido);
                sistema.cadastrarPedido(pedido);

                areaSaida.append("\nVENDA FINALIZADA: " + pedido.getCodigo());
                areaSaida.append("\nTotal: R$ " + String.format("%.2f", pedido.getValorTotal()) + "\n");
                atualizarSaldoLabel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao finalizar: " + ex.getMessage());
            }
        }
    }


    private void listarPedidos() {
        List<Pedido> pedidos = sistema.listarPedidosPorStatus(StatusPedido.PAGO);
        areaSaida.append("\n--- HISTÓRICO DE VENDAS ---\n");
        if (pedidos.isEmpty()) areaSaida.append("(Nenhuma venda realizada)\n");
        for (Pedido p : pedidos) {
            areaSaida.append(p.toString() + "\n");
        }
    }

    private void exibirFaturamento() {
        double total = sistema.calcularFaturamentoTotal();
        double lucro = sistema.calcularLucroTotal();
        areaSaida.append("\n--- RELATÓRIO FINANCEIRO ---\n");
        areaSaida.append(String.format("Faturamento Bruto (Vendas): R$ %.2f\n", total));
        areaSaida.append(String.format("Lucro Líquido Real: R$ %.2f\n", lucro));
        areaSaida.append("----------------------------\n");
    }

    private void ajustarSaldo() {
        String novoSaldoStr = JOptionPane.showInputDialog(this, "Informe o novo saldo em caixa:", sistema.getSaldoAtual());
        if (novoSaldoStr != null) {
            try {
                sistema.setSaldoAtual(Double.parseDouble(novoSaldoStr));
                atualizarSaldoLabel();
                areaSaida.append("ℹ Saldo em caixa ajustado manualmente.\n");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido.");
            }
        }
    }

    private void salvarDados() {
        try {
            sistema.salvarDados();
            areaSaida.append("Dados salvos nos arquivos .txt\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar.");
        }
    }

    private void recuperarDados() {
        try {
            sistema.recuperarDados();
            atualizarSaldoLabel();
            areaSaida.append("Dados recuperados com sucesso.\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar.");
        }
    }

    public static void main(String[] args) {
            UIManager.put("OptionPane.yesButtonText", "Sim");
            UIManager.put("OptionPane.noButtonText", "Não");
            UIManager.put("OptionPane.cancelButtonText", "Cancelar");

            SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
        }

    }