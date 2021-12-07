package br.com.zupproject.pagamentoboletos.controllers.requests;


import br.com.zupproject.pagamentoboletos.entidades.embeddables.Conta;

import javax.persistence.Embedded;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ContaRequest {

    @NotNull
    private Long idUsuario;
    @NotBlank
    private String nroConta;
    @NotBlank @Email
    private String email;



    public Conta toModel(){
        return new Conta(idUsuario,nroConta,email);
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
