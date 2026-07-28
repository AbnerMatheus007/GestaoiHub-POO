package modelo;

/**
 * Representa um cliente da loja online Infinit Hub.
 */
public class Cliente extends Pessoa {

    private String telefone;
    private String email;
    private String endereco;

    public Cliente(String id, String nome, String cpf, String telefone, String email, String endereco) {
        super(id, nome, cpf);
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id='" + getId() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", cpf='" + getCpf() + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", endereco='" + endereco + '\'' +
                '}';
    }
}
