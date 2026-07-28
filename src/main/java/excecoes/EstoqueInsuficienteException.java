package excecoes;

public class EstoqueInsuficienteException extends Exception {

    private static final long serialVersionUID = 1L;

    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
