package com.scarano.dev.auth.model.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioDTO {
    private long id;
    private String nome;
    private String sobrenome;
    private long cargoId;
    private String login;
    private String senha;
}
