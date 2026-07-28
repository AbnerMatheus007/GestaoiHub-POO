package modelo;

import java.time.LocalDate;
import java.util.List;

/**
 * Representa um pedido de compra feito por um Cliente e fechado por um Vendedor,
 * contendo uma lista de itens (produto + quantidade).
 */
public class Pedido {

    private String id;
    private Cliente cliente;
    private Vendedor vendedor;
    private List<ItemPedido> itens;
    private LocalDate dataPedido;
    private StatusPedido status;

    public Pedido(String id, Cliente cliente, Vendedor vendedor, List<ItemPedido> itens, LocalDate dataPedido) {
        this.id = id;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.itens = itens;
        this.dataPedido = dataPedido;
        this.status = StatusPedido.AGUARDANDO_PAGAMENTO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    /**
     * Valor total do pedido, calculado a partir da soma dos subtotais de cada item
     * (usa Streams: map + sum).
     */
    public double getValorTotal() {
        return itens.stream()
                .mapToDouble(ItemPedido::getSubtotal)
                .sum();
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id='" + id + '\'' +
                ", cliente=" + cliente.getNome() +
                ", vendedor=" + vendedor.getNome() +
                ", itens=" + itens.size() +
                ", valorTotal=" + getValorTotal() +
                ", dataPedido=" + dataPedido +
                ", status=" + status +
                '}';
    }
}
