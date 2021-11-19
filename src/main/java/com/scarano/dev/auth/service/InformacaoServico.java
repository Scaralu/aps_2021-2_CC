package com.scarano.dev.auth.service;

import com.scarano.dev.auth.model.dtos.InformacaoDTO;
import com.scarano.dev.auth.model.entidade.Informacao;
import com.scarano.dev.auth.model.entidade.Nivel;
import com.scarano.dev.auth.model.entidade.Usuario;
import com.scarano.dev.auth.model.excecoes.NivelInvalidoException;
import com.scarano.dev.auth.model.excecoes.UsuarioNaoEncontradoException;
import com.scarano.dev.auth.repository.InformacaoRepositorio;
import com.scarano.dev.auth.repository.NivelRepositorio;
import com.scarano.dev.auth.repository.UsuarioRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InformacaoServico {
    private static final String LEVEL_NOT_FOUND = "Nivel não encontrado!";
    private static final String USER_NOT_FOUND = "Usuário não encontrado!";

    private final InformacaoRepositorio informacaoRepositorio;
    private final NivelRepositorio nivelRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    public InformacaoServico(InformacaoRepositorio informacaoRepositorio, NivelRepositorio nivelRepositorio, UsuarioRepositorio usuarioRepositorio) {
        this.informacaoRepositorio = informacaoRepositorio;
        this.nivelRepositorio = nivelRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Optional<Informacao> save(InformacaoDTO informacaoDTO) {
        if(!isLevelValid(informacaoDTO.getLevel_ID()))
            throw new NivelInvalidoException(LEVEL_NOT_FOUND);

        Nivel nivel = nivelRepositorio.findById(informacaoDTO.getLevel_ID()).get();

        Informacao informacao = Informacao.novo(
                nivel,
                informacaoDTO.getDescription()
        );

        return Optional.of(informacaoRepositorio.save(informacao));
    }

    private boolean isLevelValid(long nivelId) {
        return nivelRepositorio.findById(nivelId).isPresent();
    }

    public Optional<List<Informacao>> obterPorUsuarioId(long user_ID) {

        Optional<Usuario> user = usuarioRepositorio.findById(user_ID);
        if(!user.isPresent())
            throw new UsuarioNaoEncontradoException(USER_NOT_FOUND);

        return informacaoRepositorio.findByNivel(user.get().getCargo().getNivel().getId());
    }
}
