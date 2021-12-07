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


    @Override
    public String toString() {
        return "DadosEmail{" +
                "mensagem='" + mensagem + '\'' +
                ", assunto='" + assunto + '\'' +
                ", remetente='" + remetente + '\'' +
                ", destinatario='" + destinatario + '\'' +
                '}';
    }
}
