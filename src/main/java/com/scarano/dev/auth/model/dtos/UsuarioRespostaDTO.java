package com.scarano.dev.auth.model.dtos;

import com.scarano.dev.auth.model.entidade.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.modelmapper.ModelMapper;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor()
@Getter @Setter
public class UsuarioRespostaDTO {
    private long id;
    private String nome;
    private String sobrenome;
    private long cargoId;
    private String cargoDescricao;
    private long nivelId;
    private String nivelDescricao;
    private String login;
    @JsonIgnore
    private String senha;

    public static UsuarioRespostaDTO converterEntidadeParaDTO(Usuario usuario) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Usuario.class, UsuarioRespostaDTO.class)
                .addMapping( src -> src.getCargo().getId(), UsuarioRespostaDTO::setCargoId);
        modelMapper.typeMap(Usuario.class, UsuarioRespostaDTO.class)
                .addMapping( src -> src.getCargo().getDescription(), UsuarioRespostaDTO::setCargoDescricao);
        modelMapper.typeMap(Usuario.class, UsuarioRespostaDTO.class)
                .addMapping( src -> src.getCargo().getNivel().getId(), UsuarioRespostaDTO::setNivelId);
        modelMapper.typeMap(Usuario.class, UsuarioRespostaDTO.class)
                .addMapping( src -> src.getCargo().getNivel().getDescription(), UsuarioRespostaDTO::setNivelDescricao);
        return modelMapper.map(usuario, UsuarioRespostaDTO.class);
    }
}

