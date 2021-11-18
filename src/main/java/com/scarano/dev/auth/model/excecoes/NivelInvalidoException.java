package com.scarano.dev.auth.model.excecoes;

public class NivelInvalidoException extends RuntimeException {
    public NivelInvalidoException(String mensagem) {
        super(mensagem);
    }
}
