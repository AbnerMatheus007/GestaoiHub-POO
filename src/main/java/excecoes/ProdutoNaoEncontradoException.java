package excecoes;

public class ProdutoNaoEncontradoException extends Exception {

    private static final long serialVersionUID = 1L;

    public ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
