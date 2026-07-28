package modelo;

/**
 * Representa um vendedor (atendente) responsável por fechar pedidos na Infinit Hub.
 */
public class Vendedor extends Pessoa {

    private double percentualComissao;

    public Vendedor(String id, String nome, String cpf, double percentualComissao) {
        super(id, nome, cpf);
        this.percentualComissao = percentualComissao;
    }

    public double getPercentualComissao() {
        return percentualComissao;
    }

    public void setPercentualComissao(double percentualComissao) {
        this.percentualComissao = percentualComissao;
    }

    @Override
    public String toString() {
        return "Vendedor{" +
                "id='" + getId() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", cpf='" + getCpf() + '\'' +
                ", percentualComissao=" + percentualComissao +
                '}';
    }
}
