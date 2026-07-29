package excecoes;

public class PedidoNaoEncontradoException extends Exception {

    private static final long serialVersionUID = 1L;

    public PedidoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
