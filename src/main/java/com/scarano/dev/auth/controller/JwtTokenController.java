package com.scarano.dev.auth.controller;

import com.scarano.dev.auth.model.JwtResponse;
import com.scarano.dev.auth.model.Reposta;
import com.scarano.dev.auth.model.excecoes.UsuarioNaoEncontradoException;
import com.scarano.dev.auth.security.config.JwtTokenServico;
import com.scarano.dev.auth.service.ImpressaoDigitalServico;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1")
public class JwtTokenController {
    private final static String ARQUIVO = "arquivo";
    private final static String LOGIN = "login";
    private static final String SENHA = "senha";
    private static final String AUTENTICACAO = "/autenticacao";

    private final JwtTokenServico jwtTokenUtil;
    private final ImpressaoDigitalServico impressaoDigitalServico;
    private final AuthenticationManager authenticationManager;

    public JwtTokenController(JwtTokenServico jwtTokenUtil, ImpressaoDigitalServico impressaoDigitalServico, AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.impressaoDigitalServico = impressaoDigitalServico;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = AUTENTICACAO)
    public ResponseEntity<?> Autenticacao(@RequestParam(ARQUIVO) MultipartFile arquivo, @RequestParam(LOGIN) String login, @RequestParam(SENHA) String senha) {
        Reposta<JwtResponse> resposta = new Reposta<>();
        try {
            boolean ehValida = impressaoDigitalServico.ehDigitalValida(login, arquivo);

            if (!ehValida) {
                resposta.adicionarMensagemErro("Digital inválida");
                return new ResponseEntity<>(resposta, HttpStatus.NOT_FOUND);
            }

            final Authentication auth = autenticar(login, senha);

            SecurityContextHolder.getContext().setAuthentication(auth);
            resposta.setData(new JwtResponse(jwtTokenUtil.gerarToken(auth)));

            return new ResponseEntity<>(resposta, HttpStatus.OK);
        } catch (UsuarioNaoEncontradoException naoEncontrado) {
            resposta.adicionarMensagemErro(naoEncontrado.getMessage());
            return new ResponseEntity<>(resposta, HttpStatus.NOT_FOUND);
        } catch (IOException io) {
            resposta.adicionarMensagemErro(io.getMessage());
            return new ResponseEntity<>(resposta, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            resposta.adicionarMensagemErro(e.getMessage());
            return new ResponseEntity<>(resposta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Authentication autenticar(String login, String senha) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, senha));
        } catch (DisabledException e) {
            throw new Exception("Usuário desabilitado");
        } catch (BadCredentialsException e) {
            throw new Exception("Senha inválida");
        }
    }
}