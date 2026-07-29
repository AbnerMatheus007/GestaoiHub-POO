package modelo;

/**
 * Indica o estado atual de um pedido feito na Infinit Hub.
 */
public enum StatusPedido {
    AGUARDANDO_PAGAMENTO,
    PAGO,
    ENVIADO,
    ENTREGUE,
    CANCELADO
}
