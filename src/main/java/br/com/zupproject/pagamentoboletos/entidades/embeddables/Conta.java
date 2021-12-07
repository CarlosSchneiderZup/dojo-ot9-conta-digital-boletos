package br.com.zupproject.pagamentoboletos.entidades.embeddables;

import javax.persistence.Embeddable;

@Embeddable
public class Conta {

    private Long idUsuario;
    private String nroConta;
    private String email;

    public Conta(Long idUsuario, String nroConta, String email) {
        this.idUsuario = idUsuario;
        this.nroConta = nroConta;
        this.email = email;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNroConta() {
        return nroConta;
    }

    public String getEmail() {
        return email;
    }


}
