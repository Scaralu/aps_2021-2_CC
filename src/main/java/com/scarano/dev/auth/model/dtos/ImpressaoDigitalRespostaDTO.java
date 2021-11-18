package com.scarano.dev.auth.model.dtos;

import com.scarano.dev.auth.model.entidade.ImpressaoDigital;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ImpressaoDigitalRespostaDTO {
    private long id;
    private String nome;
    private long usuarioId;

    public static ImpressaoDigitalRespostaDTO converterEntidadeParaDTO(ImpressaoDigital impressaoDigital) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(ImpressaoDigital.class, ImpressaoDigitalRespostaDTO.class)
                .addMapping( src -> src.getUsuario().getId(), ImpressaoDigitalRespostaDTO::setUsuarioId);
        return modelMapper.map(impressaoDigital, ImpressaoDigitalRespostaDTO.class);
    }
}
