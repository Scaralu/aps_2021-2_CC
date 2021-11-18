package com.scarano.dev.auth.model.dtos;

import com.scarano.dev.auth.model.entidade.Informacao;
import com.scarano.dev.auth.model.entidade.Nivel;
import lombok.*;
import org.modelmapper.ModelMapper;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor()
@Getter @Setter
public class InformacaoRespostaDTO {
    private long id;
    private String descricao;
    private Nivel nivel;

    public static InformacaoRespostaDTO converterEntidadeParaDTO(Informacao informacao) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Informacao.class, InformacaoRespostaDTO.class)
                .addMapping( src -> src.getNivel(), InformacaoRespostaDTO::setNivel);
        return modelMapper.map(informacao, InformacaoRespostaDTO.class);
    }
}
