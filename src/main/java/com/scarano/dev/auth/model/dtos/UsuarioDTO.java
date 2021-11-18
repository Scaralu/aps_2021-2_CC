package com.scarano.dev.auth.model.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioDTO {
    private long id;
    private String name;
    private String last_name;
    private long role_ID;
    private String username;
    private String password;
}
