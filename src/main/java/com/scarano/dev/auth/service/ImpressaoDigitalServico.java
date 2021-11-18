package com.scarano.dev.auth.service;

import com.scarano.dev.auth.model.excecoes.ImpressaoDigitaLNaoEncontradaException;
import com.scarano.dev.auth.model.excecoes.UsuarioNaoEncontradoException;
import com.scarano.dev.auth.model.entidade.ImpressaoDigital;
import com.scarano.dev.auth.model.entidade.Usuario;
import com.scarano.dev.auth.model.excecoes.UsuarioPossuiImpDigitalException;
import com.scarano.dev.auth.repository.ImpDigitalRepositorio;
import com.scarano.dev.auth.repository.UsuarioRepositorio;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImpressaoDigitalServico {

    private static final String USUARIO_NAO_ENCONTRADO_MENSAGEM = "Usuário não encontrado!";
    private static final double MINIMO_PERCENTUAL = 40;
    private static final String USUARIO_JA_POSSUI_IMPDIGITAL_MENSAGEM = "Usuário já possui impressão digital!";
    private static final String USUARIO_NAO_POSSUI_IMPDIGITAL_MENSAGEM = "Usuário não possui impressão digital!";

    private final ImpDigitalRepositorio impDigitalRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    public ImpressaoDigitalServico(ImpDigitalRepositorio impDigitalRepositorio, UsuarioRepositorio usuarioRepositorio) {
        this.impDigitalRepositorio = impDigitalRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Optional<ImpressaoDigital> salvar(MultipartFile arquivo, long usuarioId) throws IOException {
        if(!ehUsuarioValido(usuarioId))
            throw new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO_MENSAGEM);

        Optional<Usuario> usuario = usuarioRepositorio.findById(usuarioId);

        if (!usuario.isPresent())
            throw new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO_MENSAGEM);

        if(usuarioPossuiImpDigital(usuario.get().getId(), usuario.get().getUsername()))
            throw new UsuarioPossuiImpDigitalException(USUARIO_JA_POSSUI_IMPDIGITAL_MENSAGEM);

        String nomeArquivo = StringUtils.cleanPath(arquivo.getOriginalFilename());
        ImpressaoDigital arquivoDb = ImpressaoDigital.novo(
            nomeArquivo,
            arquivo.getBytes(),
            usuario.get()
        );

        return Optional.of(impDigitalRepositorio.save(arquivoDb));
    }

    private boolean ehUsuarioValido(long usuarioId) {
        return usuarioRepositorio.findById(usuarioId).isPresent();
    }

    private boolean ehUsuarioValido(String username) {
        Optional<Usuario> usuario = usuarioRepositorio.findByUsername(username);
        return usuario.isPresent();
    }

    public boolean ehDigitalValida(String username, MultipartFile arquivo) throws IOException {
        double porcentagem = 0;

        try{
            if (!ehUsuarioValido(username))
                throw new UsuarioNaoEncontradoException("Usuario não encontrado");

            Optional<Usuario> usuario = usuarioRepositorio.findByUsername(username);
            Optional<ImpressaoDigital> impressaoDigital = impDigitalRepositorio
                    .findByUsuarioIdELogin(usuario.get().getId(), usuario.get().getUsername());

            FingerprintTemplate impressaoDigitalBd = new FingerprintTemplate(
                    new FingerprintImage()
                            .dpi(500)
                            .decode(impressaoDigital.get().getImage()));

            FingerprintTemplate impressaoDigitalEmTeste = null;
                impressaoDigitalEmTeste = new FingerprintTemplate(
                        new FingerprintImage()
                                .dpi(500)
                                .decode(arquivo.getBytes()));

            porcentagem = new FingerprintMatcher()
                    .index(impressaoDigitalBd)
                    .match(impressaoDigitalEmTeste);
        } catch (IOException e) {
            throw new IOException("Erro ao ler arquivo "+e.getMessage());
        }
        return porcentagem >= MINIMO_PERCENTUAL;
    }

    public Optional<ImpressaoDigital> atualizar(MultipartFile arquivo, long usuarioId) throws IOException {
        if (!ehUsuarioValido(usuarioId))
            throw new UsuarioNaoEncontradoException("Usuario não encontrado");

        Optional<Usuario> usuario = usuarioRepositorio.findById(usuarioId);

        Optional<ImpressaoDigital> impDigital = impDigitalRepositorio.findByUsuarioIdELogin(usuario.get().getId(), usuario.get().getUsername());
        if(!impDigital.isPresent())
            throw new ImpressaoDigitaLNaoEncontradaException(USUARIO_NAO_POSSUI_IMPDIGITAL_MENSAGEM);

        impDigital.get().setImage(arquivo.getBytes());
        impDigital.get().setName(arquivo.getName());

        return Optional.of(impDigitalRepositorio.save(impDigital.get()));
    }

    private boolean usuarioPossuiImpDigital(long usuarioId, String login) {
        return impDigitalRepositorio.findByUsuarioIdELogin(usuarioId, login).isPresent();
    }
}
