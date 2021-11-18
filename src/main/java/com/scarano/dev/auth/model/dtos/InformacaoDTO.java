package com.scarano.dev.auth.model.dtos;

import lombok.Getter;

@Getter
public class InformacaoDTO {
    private long id;
    private long nivelId;
    private String descricao;
    private String riscoSubterraneoGus;
    private String riscoSubterraneoEpa;
    private String riscoSuperficialGossSedimental;
    private String riscoSuperficialGossDissolvido;
    private String endereco;
}
