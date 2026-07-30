# Infinit Hub - Gestão de loja (POO)

Este projeto consiste em um sistema de gestão para uma loja online, desenvolvido para a disciplina de Programação Orientada a Objetos (POO). O sistema permite o controle de estoque, realização de pedidos e gestão financeira.

## Sobre o Projeto

Diferente de sistemas de balcão, este sistema foca na operação autônoma de uma loja online, onde o controle financeiro é vinculado diretamente à movimentação de estoque.

## Funcionalidades Principais (Interface SistemaInfinitHub)

O sistema implementa o padrão **Façade** através da interface `SistemaInfinitHub`, que conta com 16 métodos para atender a todos os requisitos do projeto:

1.  **cadastrarProduto:** Registro de novos itens no catálogo (desconta o investimento inicial do saldo).
2.  **removerProduto:** Exclusão de itens com tratamento de `ProdutoNaoEncontradoException`.
3.  **pesquisarProdutoPorNome:** Localização direta pelo nome do produto.
4.  **listarTodosProdutos:** Lista completa do catálogo.
5.  **pesquisarProdutosPorCategoria:** Filtragem por categoria usando **Streams**.
6.  **listarProdutosComEstoqueDisponivel:** Filtra produtos ativos usando **Streams**.
7.  **reporEstoque:** Adiciona unidades e desconta o custo do **Saldo Atual**.
8.  **cadastrarPedido:** Registra a venda, baixa estoque e soma ao **Saldo**.
9.  **removerPedido:** Cancela a venda, estorna o saldo e devolve itens ao estoque.
10. **listarPedidosPorStatus:** Filtra pedidos por status usando **Streams**.
11. **getSaldoAtual / setSaldoAtual:** Gestão do capital em caixa (inclui correção manual).
12. **calcularFaturamentoTotal:** Soma de todos os pedidos realizados usando **Streams (mapToDouble + sum)**.
13. **calcularLucroTotal:** Soma o lucro (venda − custo) de cada item vendido usando **Streams (flatMap + mapToDouble + sum)**.
14. **salvarDados:** Persistência em arquivos `.txt` via classe `GravadorDeDados`.
15. **recuperarDados:** Carregamento do estado anterior do sistema.

## Requisitos Técnicos Atendidos

- **Interface:** Interface centralizadora com 16 métodos assinados.
- **Coleções:** Uso de `HashMap` para armazenamento de dados em memória.
- **Streams e Lambda:** Implementados em diversos pontos para processamento de dados (filter, map, mapToDouble, flatMap).
- **Exceções:** Tratamento robusto de erros de negócio (`ProdutoNaoEncontradoException`, `PedidoNaoEncontradoException`, `EstoqueInsuficienteException`).
- **Persistência:** Gravação/Leitura em arquivos de texto (`produtos.txt`, `pedidos.txt`, `saldo.txt`), com métodos que lançam `IOException`.
- **Interface Gráfica:** Desenvolvida em **Swing**, com barra de menu (`JMenuBar`) e painel lateral com atalhos.
- **Testes Automatizados:** 7 casos de teste no JUnit 5, exercitando todos os métodos da interface.

## Estrutura de Pastas

- `src/main/java/modelo`: Entidades (Produto, Pedido, ItemPedido, Enums).
- `src/main/java/servico`: Interface e implementação da lógica de negócio.
- `src/main/java/persistencia`: Classe `GravadorDeDados`.
- `src/main/java/excecoes`: Exceções personalizadas.
- `src/main/java/ui`: Interface gráfica `TelaPrincipal`.
- `src/test/java`: Testes automatizados.

---
**Desenvolvido para a Atividade de POO - 2026.1**
Equipe: Abner Matheus dos Santos Silva e Luis Henrique Arruda dos Santos