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
    private static final String USER_NOT_FOUND = "Usuário não encontrado!";
    private static final String ROLE_NOT_FOUND = "Cargo não encontrado!";
    private static final String USERNAME_ALREADY_EXISTS = "Este username ja esta cadastrado";

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

    public Optional<Usuario> save(UsuarioDTO usuarioDTO) {
        if(!isRoleValid(usuarioDTO.getRole_ID()))
            throw new CargoNaoEncontradoException(ROLE_NOT_FOUND);

        if(checkIfUsernameAlreadyExists(usuarioDTO.getUsername()))
            throw new LoginInvalidoException(USERNAME_ALREADY_EXISTS);

        String passwordCrypto = passwordEncoder.encode(usuarioDTO.getPassword());
        Optional<Cargo> cargo = cargoRepositorio.findById(usuarioDTO.getRole_ID());

        if(!cargo.isPresent() && passwordCrypto.isEmpty())
            return Optional.empty();

        Usuario usuario = Usuario
                .novo(
                        usuarioDTO.getName(),
                        usuarioDTO.getLast_name(),
                        cargo.get(),
                        usuarioDTO.getUsername(),
                        passwordCrypto
                );

        return Optional.of(usuarioRepositorio.save(usuario));
    }

    private boolean checkIfUsernameAlreadyExists(String login) {
        return usuarioRepositorio.findByUsername(login).isPresent();
    }

    public Optional<Usuario> update(UsuarioDTO usuarioDTO) {
        Optional<Usuario> user = usuarioRepositorio.findById(usuarioDTO.getId());
        if(!user.isPresent())
            throw new UsuarioNaoEncontradoException(USER_NOT_FOUND);

        if(!isRoleValid(usuarioDTO.getRole_ID()))
            throw new CargoNaoEncontradoException(ROLE_NOT_FOUND);

        String senhaCriptografada = passwordEncoder.encode(usuarioDTO.getPassword());
        Optional<Cargo> cargo = cargoRepositorio.findById(usuarioDTO.getRole_ID());

        if(!usuarioDTO.getPassword().isEmpty())
            user.get().setPassword(senhaCriptografada);

        user.get().setCargo(cargo.get());
        user.get().setUsername(usuarioDTO.getUsername());
        user.get().setName(usuarioDTO.getName());
        user.get().setLast_name(usuarioDTO.getLast_name());

        return Optional.of(usuarioRepositorio.save(user.get()));
    }

    public Optional<Usuario> obterPorId(long id) {
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        return usuario;
    }

    private boolean isRoleValid(long cargoId) {
        Optional<Cargo> cargo = cargoRepositorio.findById(cargoId);
        return cargo.isPresent();
    }

    public void delete(long id) {
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        if(!usuario.isPresent())
            throw new UsuarioNaoEncontradoException(USER_NOT_FOUND);

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
