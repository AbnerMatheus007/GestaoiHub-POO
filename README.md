# Infinit Hub - Sistema de Gestão de Loja Online

Equipe: Abner Matheus dos Santos Silva e Luis Henrique

## Sobre o projeto

A Infinit Hub é uma loja online de periféricos e eletrônicos (mouses, teclados,
monitores, componentes de PC, áudio, etc). Este sistema simula o gerenciamento
dessa loja, permitindo controlar o catálogo de produtos, os clientes, os
vendedores (atendentes) e os pedidos realizados, sempre respeitando o estoque
disponível de cada produto.

O sistema permite:

- Cadastrar, pesquisar (por nome ou por categoria) e remover **Produtos**.
- Cadastrar, pesquisar e remover **Clientes**.
- Cadastrar, pesquisar e remover **Vendedores**.
- Cadastrar, pesquisar e remover **Pedidos**, vinculando um Cliente, um
  Vendedor e uma lista de itens (produto + quantidade).
- Verificar automaticamente se há **estoque suficiente** de cada produto antes
  de concluir um pedido (caso contrário, uma exceção é lançada e o pedido não
  é fechado).
- Consultar pedidos por cliente, por vendedor e por status.
- Calcular o total vendido por um vendedor.
- Listar produtos que ainda possuem estoque disponível.
- Salvar e recuperar todos os dados do sistema em arquivo.

A principal regra de negócio é o controle de estoque: ao fechar um pedido, o
sistema confere se cada produto pedido possui unidades suficientes; se não
tiver, uma `EstoqueInsuficienteException` é lançada e o pedido não é
concluído. Ao remover/cancelar um pedido, as unidades voltam automaticamente
para o estoque.

## Arquitetura

O sistema segue o padrão **Façade**: a interface `SistemaInfinitHub` reúne
todas as funcionalidades do sistema, e a classe `InfinitHubEcommerce`
implementa essa interface usando `Map`s para armazenar Produtos, Clientes,
Vendedores e Pedidos em memória. Duas das funcionalidades da interface são
`salvarDados()` e `recuperarDados()`, que delegam a gravação/leitura em
arquivo para a classe `GravadorDeDados` (cujos métodos lançam `IOException`).

Em pelo menos dois pontos (`pesquisarProdutosPorNome`/`pesquisarProdutosPorCategoria`
e `listarNomesDosProdutos`/`calcularTotalVendidoPorVendedor`) a implementação
usa **Java Streams** (`filter`, `map`, `mapToDouble`, `collect`) para realizar
pesquisas e cálculos sobre os dados.

### Pacotes e principais arquivos

- `src/main/java/modelo` — Entidades do domínio: `Pessoa` (abstrata),
  `Cliente`, `Vendedor`, `Produto`, `ItemPedido`, `Pedido`, e os enums
  `CategoriaProduto` e `StatusPedido`.
- `src/main/java/excecoes` — Exceções de negócio (`ClienteNaoEncontradoException`,
  `VendedorNaoEncontradoException`, `ProdutoNaoEncontradoException`,
  `PedidoNaoEncontradoException`, `EstoqueInsuficienteException`).
- `src/main/java/servico` — `SistemaInfinitHub` (interface/Façade) e
  `InfinitHubEcommerce` (classe que implementa a interface e concentra a
  lógica do sistema, onde são feitas as pesquisas, cadastros e cálculos).
- `src/main/java/persistencia` — `GravadorDeDados`, responsável pela
  gravação/leitura dos dados em arquivos `.txt`.
- `src/main/java/ui` — `TelaPrincipal`, interface gráfica em Swing
  (`JFrame`) com barra de menus (`Produto`, `Cliente`, `Vendedor`, `Pedido` e
  `Arquivo`, este último com a opção "Salvar dados"). É aqui que ficam os
  controladores (os métodos ligados aos itens de menu que chamam o sistema).
- `src/test/java/InfinitHubEcommerceTest.java` — Classe de teste automático
  (JUnit 5) que exercita cadastro, remoção e pesquisa das principais
  funcionalidades do sistema.
- `diagrama-uml.png` / `diagrama-uml.dot` — Diagrama de classes UML do
  sistema.

## Como executar

Pré-requisitos: Java 17+ e Maven.

```bash
mvn test              # roda os testes automáticos
mvn compile exec:java -Dexec.mainClass="ui.TelaPrincipal"   # roda a interface gráfica
```

(ou, pela sua IDE, basta rodar a classe `ui.TelaPrincipal`.)
