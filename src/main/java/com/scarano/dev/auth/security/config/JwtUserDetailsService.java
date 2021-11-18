package com.scarano.dev.auth.security.config;

import com.scarano.dev.auth.model.entidade.Usuario;
import com.scarano.dev.auth.model.enums.Nivel;
import com.scarano.dev.auth.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepositorio.findByLogin(login);
        if (!usuario.isPresent()) {
            throw new UsernameNotFoundException("Usuário não encontrado com login: " + login);
        }

        String acesso = obterRole(usuario.get().getCargo().getNivel().getDescricao().toUpperCase());
        List<GrantedAuthority> roles = AuthorityUtils.createAuthorityList(acesso);
        return new User(usuario.get().getLogin(), usuario.get().getSenha(), roles);
    }

    private String obterRole(String nivel) {
        if(Nivel.ADMINISTRADOR.toString().equals(nivel))
            return "ROLE_ADMIN";
        return "ROLE_USER";
    }
}
