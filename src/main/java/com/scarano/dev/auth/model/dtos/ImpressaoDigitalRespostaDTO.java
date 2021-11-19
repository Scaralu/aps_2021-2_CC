package com.scarano.dev.auth.model.dtos;

import com.scarano.dev.auth.model.entidade.ImpressaoDigital;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ImpressaoDigitalRespostaDTO {
    private long id;
    private String name;
    private long user_ID;

    public static ImpressaoDigitalRespostaDTO entityConverter(ImpressaoDigital impressaoDigital) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(ImpressaoDigital.class, ImpressaoDigitalRespostaDTO.class)
                .addMapping( src -> src.getUsuario().getId(), ImpressaoDigitalRespostaDTO::setUser_ID);
        return modelMapper.map(impressaoDigital, ImpressaoDigitalRespostaDTO.class);
    }
}
