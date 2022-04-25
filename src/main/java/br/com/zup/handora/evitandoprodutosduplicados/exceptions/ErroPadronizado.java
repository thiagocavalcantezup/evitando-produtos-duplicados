package br.com.zup.handora.evitandoprodutosduplicados.exceptions;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;

import org.springframework.validation.FieldError;

public class ErroPadronizado {

    private Integer codigoHttp;
    private String mensagemHttp;
    private String mensagemGeral;
    private List<String> mensagens;

    public ErroPadronizado(Integer codigoHttp, String mensagemHttp, String mensagemGeral) {
        this.codigoHttp = codigoHttp;
        this.mensagemHttp = mensagemHttp;
        this.mensagemGeral = mensagemGeral;
        this.mensagens = new ArrayList<>();
    }

    public void adicionarErro(FieldError fieldError) {
        mensagens.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
    }

    public void adicionarErro(ConstraintViolation<?> constraintViolation) {
        mensagens.add(
            constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage()
        );
    }

    public void adicionarErro(String erro) {
        mensagens.add(erro);
    }

    public Integer getCodigoHttp() {
        return codigoHttp;
    }

    public String getMensagemHttp() {
        return mensagemHttp;
    }

    public String getMensagemGeral() {
        return mensagemGeral;
    }

    public List<String> getMensagens() {
        return mensagens;
    }

}
