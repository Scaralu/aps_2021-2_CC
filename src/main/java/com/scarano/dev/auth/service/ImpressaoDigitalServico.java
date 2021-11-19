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

    private static final double MINIMUN_MATCH_PERCENTAGE = 60;

    private static final String USER_NOT_FOUND = "Usuário não encontrado!";
    private static final String USER_ALREADY_REGISTERED = "Usuário já possui impressão digital!";
    private static final String USER_WITHOUT_REGISTER = "Usuário não possui impressão digital!";

    private final ImpDigitalRepositorio impDigitalRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    public ImpressaoDigitalServico(ImpDigitalRepositorio impDigitalRepositorio, UsuarioRepositorio usuarioRepositorio) {
        this.impDigitalRepositorio = impDigitalRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Optional<ImpressaoDigital> save(MultipartFile arquivo, long usuarioId) throws IOException {
        if(!isUserValid(usuarioId))
            throw new UsuarioNaoEncontradoException(USER_NOT_FOUND);

        Optional<Usuario> usuario = usuarioRepositorio.findById(usuarioId);

        if (!usuario.isPresent())
            throw new UsuarioNaoEncontradoException(USER_NOT_FOUND);

        if(usuarioPossuiImpDigital(usuario.get().getId(), usuario.get().getUsername()))
            throw new UsuarioPossuiImpDigitalException(USER_ALREADY_REGISTERED);

        String nomeArquivo = StringUtils.cleanPath(arquivo.getOriginalFilename());
        ImpressaoDigital arquivoDb = ImpressaoDigital.novo(
            nomeArquivo,
            arquivo.getBytes(),
            usuario.get()
        );

        return Optional.of(impDigitalRepositorio.save(arquivoDb));
    }

    private boolean isUserValid(long usuarioId) {
        return usuarioRepositorio.findById(usuarioId).isPresent();
    }

    private boolean isUserValid(String username) {
        Optional<Usuario> usuario = usuarioRepositorio.findByUsername(username);
        return usuario.isPresent();
    }

    public boolean isDigitalValid(String username, MultipartFile arquivo) throws IOException {
        double percentage = 0;

        try{
            if (!isUserValid(username))
                throw new UsuarioNaoEncontradoException(USER_NOT_FOUND);

            Optional<Usuario> user = usuarioRepositorio.findByUsername(username);
            Optional<ImpressaoDigital> impressaoDigital = impDigitalRepositorio
                    .findByUsuarioIdELogin(user.get().getId(), user.get().getUsername());

            FingerprintTemplate impressaoDigitalBd = new FingerprintTemplate(
                    new FingerprintImage()
                            .dpi(500)
                            .decode(impressaoDigital.get().getImage())
            );

            FingerprintTemplate impressaoDigitalEmTeste = null;
                impressaoDigitalEmTeste = new FingerprintTemplate(
                        new FingerprintImage()
                                .dpi(500)
                                .decode(arquivo.getBytes())
                );

            percentage = new FingerprintMatcher()
                    .index(impressaoDigitalBd)
                    .match(impressaoDigitalEmTeste);
        } catch (IOException e) {
            throw new IOException("Erro ao ler arquivo "+e.getMessage());
        }

        return percentage >= MINIMUN_MATCH_PERCENTAGE;
    }

    public Optional<ImpressaoDigital> update(MultipartFile arquivo, long usuarioId) throws IOException {
        if (!isUserValid(usuarioId))
            throw new UsuarioNaoEncontradoException(USER_NOT_FOUND);

        Optional<Usuario> user = usuarioRepositorio.findById(usuarioId);

        Optional<ImpressaoDigital> impDigital = impDigitalRepositorio.findByUsuarioIdELogin(user.get().getId(), user.get().getUsername());
        if(!impDigital.isPresent())
            throw new ImpressaoDigitaLNaoEncontradaException(USER_WITHOUT_REGISTER);

        impDigital.get().setImage(arquivo.getBytes());
        impDigital.get().setName(arquivo.getName());

        return Optional.of(impDigitalRepositorio.save(impDigital.get()));
    }

    private boolean usuarioPossuiImpDigital(long user_ID, String username) {
        return impDigitalRepositorio.findByUsuarioIdELogin(user_ID, username).isPresent();
    }
}
