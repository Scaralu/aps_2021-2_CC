package com.scarano.dev.auth.service;

import com.scarano.dev.auth.model.dtos.UsuarioDTO;
import com.scarano.dev.auth.model.entidade.Cargo;
import com.scarano.dev.auth.model.entidade.ImpressaoDigital;
import com.scarano.dev.auth.model.excecoes.CargoNaoEncontradoException;
import com.scarano.dev.auth.model.entidade.Usuario;
import com.scarano.dev.auth.model.excecoes.LoginInvalidoException;
import com.scarano.dev.auth.model.excecoes.UsuarioNaoEncontradoException;
import com.scarano.dev.auth.repository.CargoRepositorio;
import com.scarano.dev.auth.repository.ImpDigitalRepositorio;
import com.scarano.dev.auth.repository.UsuarioRepositorio;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServico {
    private static final String USUARIO_NAO_ENCONTRADO_MENSAGEM = "Usuário não encontrado!";
    private static final String CARGO_NAO_ENCONTRADO = "Cargo não encontrado!";
    private static final String LOGIN_REPETIDO = "o login já esta sendo utilizado por outro usuário";

    private final UsuarioRepositorio usuarioRepositorio;
    private final ImpDigitalRepositorio impDigitalRepositorio;
    private final CargoRepositorio cargoRepositorio;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServico(UsuarioRepositorio usuarioRepositorio, ImpDigitalRepositorio impDigitalRepositorio, CargoRepositorio cargoRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.impDigitalRepositorio = impDigitalRepositorio;
        this.cargoRepositorio = cargoRepositorio;
        this.passwordEncoder = passwordEncoder;
    }
    public Optional<Usuario> salvar(UsuarioDTO usuarioDTO) {
        if(!ehCargoValido(usuarioDTO.getRole_ID()))
            throw new CargoNaoEncontradoException(CARGO_NAO_ENCONTRADO);

        if(loginExiste(usuarioDTO.getUsername()))
            throw new LoginInvalidoException(LOGIN_REPETIDO);

        String senhaCriptografada = passwordEncoder.encode(usuarioDTO.getPassword());
        Optional<Cargo> cargo = cargoRepositorio.findById(usuarioDTO.getRole_ID());

        if(!cargo.isPresent() && senhaCriptografada.isEmpty())
            return Optional.empty();

        Usuario usuario = Usuario
                .novo(usuarioDTO.getUsername(), usuarioDTO.getLast_name(), cargo.get(), usuarioDTO.getUsername(), senhaCriptografada);

        return Optional.of(usuarioRepositorio.save(usuario));
    }

    private boolean loginExiste(String login) {
        return usuarioRepositorio.findByUsername(login).isPresent();
    }

    public Optional<Usuario> atualizar(UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuario = usuarioRepositorio.findById(usuarioDTO.getId());
        if(!usuario.isPresent())
            throw new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO_MENSAGEM);

        if(!ehCargoValido(usuarioDTO.getRole_ID()))
            throw new CargoNaoEncontradoException(CARGO_NAO_ENCONTRADO);

        String senhaCriptografada = passwordEncoder.encode(usuarioDTO.getPassword());
        Optional<Cargo> cargo = cargoRepositorio.findById(usuarioDTO.getRole_ID());

        if(!usuarioDTO.getPassword().isEmpty())
            usuario.get().setPassword(senhaCriptografada);

        usuario.get().setCargo(cargo.get());
        usuario.get().setUsername(usuarioDTO.getUsername());
        usuario.get().setName(usuarioDTO.getName());
        usuario.get().setLast_name(usuarioDTO.getLast_name());

        return Optional.of(usuarioRepositorio.save(usuario.get()));
    }

    public Optional<Usuario> obterPorId(long id) {
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        return usuario;
    }

    private boolean ehCargoValido(long cargoId) {
        Optional<Cargo> cargo = cargoRepositorio.findById(cargoId);
        return cargo.isPresent();
    }

    public void delete(long id) {
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        if(!usuario.isPresent())
            throw new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO_MENSAGEM);

        Optional<ImpressaoDigital> impressaoDigital = impDigitalRepositorio.findByUsuarioIdELogin(id, usuario.get().getUsername());
        if(impressaoDigital.isPresent())
            impDigitalRepositorio.delete(impressaoDigital.get());

        usuarioRepositorio.delete(usuario.get());
    }

    public List<Usuario> obterTodos() {
            return usuarioRepositorio.findAll();
    }

    public Optional<Usuario> obterPorLogin(String login) {
        Optional<Usuario> usuario = usuarioRepositorio.findByUsername(login);
        return usuario;
    }
}
