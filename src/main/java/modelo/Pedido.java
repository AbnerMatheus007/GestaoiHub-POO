package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Pedido {

    private String codigo;
    private List<ItemPedido> itens;
    private LocalDateTime dataHoraPedido;
    private StatusPedido status;

    public Pedido(List<ItemPedido> itens) {
        this.dataHoraPedido = LocalDateTime.now();
        this.codigo = "PED-" + dataHoraPedido.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        this.itens = itens;
        this.status = StatusPedido.PAGO;
    }
    
    // Construtor para recuperação de dados
    public Pedido(String codigo, List<ItemPedido> itens, LocalDateTime dataHora, StatusPedido status) {
        this.codigo = codigo;
        this.itens = itens;
        this.dataHoraPedido = dataHora;
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public LocalDateTime getDataHoraPedido() {
        return dataHoraPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public double getValorTotal() {
        return itens.stream()
                .mapToDouble(ItemPedido::getSubtotal)
                .sum();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Total: R$ %.2f | Itens: %d", 
            codigo, 
            dataHoraPedido.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), 
            getValorTotal(), 
            itens.size());
    }
}
