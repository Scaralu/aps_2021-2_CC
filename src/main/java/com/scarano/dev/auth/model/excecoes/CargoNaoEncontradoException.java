package com.scarano.dev.auth.model.excecoes;

public class CargoNaoEncontradoException extends RuntimeException {
    public CargoNaoEncontradoException(String message){
        super(message);
    }
}
