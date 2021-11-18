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
    private static final String NIVEL_NAO_ENCONTRADO = "Nivel não encontrado!";
    private static final String USUARIO_NAO_ENCONTRADO_MENSAGEM = "Usuário não encontrado!";
    private final InformacaoRepositorio informacaoRepositorio;
    private final NivelRepositorio nivelRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    public InformacaoServico(InformacaoRepositorio informacaoRepositorio, NivelRepositorio nivelRepositorio, UsuarioRepositorio usuarioRepositorio) {
        this.informacaoRepositorio = informacaoRepositorio;
        this.nivelRepositorio = nivelRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Optional<Informacao> salvar(InformacaoDTO informacaoDTO) {
        if(!ehNivelValido(informacaoDTO.getNivelId()))
            throw new NivelInvalidoException(NIVEL_NAO_ENCONTRADO);

        Nivel nivel = nivelRepositorio.findById(informacaoDTO.getNivelId()).get();
        Informacao informacao = Informacao.novo(nivel, informacaoDTO.getDescricao(),
                informacaoDTO.getRiscoSubterraneoGus(), informacaoDTO.getRiscoSubterraneoEpa(),
                informacaoDTO.getRiscoSuperficialGossSedimental(), informacaoDTO.getRiscoSuperficialGossDissolvido(), informacaoDTO.getEndereco());

        return Optional.of(informacaoRepositorio.save(informacao));
    }

    private boolean ehNivelValido(long nivelId) {
        return nivelRepositorio.findById(nivelId).isPresent();
    }

    public Optional<List<Informacao>> obterPorUsuarioId(long usuarioId) {

        Optional<Usuario> usuario = usuarioRepositorio.findById(usuarioId);
        if(!usuario.isPresent())
            throw new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO_MENSAGEM);

        return informacaoRepositorio.findByNivel(usuario.get().getCargo().getNivel().getId());
    }
}
