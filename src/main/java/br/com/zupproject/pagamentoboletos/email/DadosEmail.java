package br.com.zupproject.pagamentoboletos.email;

public class DadosEmail {

    private String mensagem;
    private String assunto;
    private String remetente;
    private String destinatario;


    public DadosEmail(String mensagem, String assunto, String remetente, String destinatario) {
        this.mensagem = mensagem;
        this.assunto = assunto;
        this.remetente = remetente;
        this.destinatario = destinatario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getRemetente() {
        return remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }
}
