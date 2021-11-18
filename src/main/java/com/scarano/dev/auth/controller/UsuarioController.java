package com.scarano.dev.auth.controller;

import com.scarano.dev.auth.model.*;
import com.scarano.dev.auth.model.dtos.UsuarioDTO;
import com.scarano.dev.auth.model.dtos.UsuarioRespostaDTO;
import com.scarano.dev.auth.model.entidade.Usuario;
import com.scarano.dev.auth.model.excecoes.CargoNaoEncontradoException;
import com.scarano.dev.auth.model.excecoes.UsuarioNaoEncontradoException;
import com.scarano.dev.auth.service.UsuarioServico;
import com.scarano.dev.auth.model.Reposta;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class UsuarioController {
    private static final String USUARIO = "/usuario";
    private static final String USUARIO_POR_ID = USUARIO + "/{id}";
    private static final String USUARIO_POR_LOGIN = USUARIO + "/{login}";
    private final UsuarioServico usuarioServico;

    public UsuarioController(UsuarioServico usuarioServico) {
        this.usuarioServico = usuarioServico;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = USUARIO, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reposta<UsuarioRespostaDTO>> salvar(@RequestBody UsuarioDTO usuarioDTO, BindingResult result) {
        Reposta<UsuarioRespostaDTO> reposta = new Reposta<>();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error
                    -> reposta.adicionarMensagemErro(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(reposta);
        }
        try {
            Optional<Usuario> usuario = usuarioServico.salvar(usuarioDTO);
            if (!usuario.isPresent()){
                reposta.setData(new UsuarioRespostaDTO());
                new ResponseEntity<>(reposta, HttpStatus.BAD_REQUEST);
            }

            UsuarioRespostaDTO repostaDTO = UsuarioRespostaDTO.converterEntidadeParaDTO(usuario.get());
            reposta.setData(repostaDTO);
            return new ResponseEntity<>(reposta, HttpStatus.CREATED);
        }catch (CargoNaoEncontradoException naoEncontrado){
            reposta.adicionarMensagemErro(naoEncontrado.getMessage());
            reposta.setData(new UsuarioRespostaDTO());
            return new ResponseEntity<>(reposta, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            reposta.adicionarMensagemErro(e.getMessage());
            reposta.setData(new UsuarioRespostaDTO());
            return new ResponseEntity<>(reposta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(value = USUARIO, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reposta<UsuarioRespostaDTO>> atualizar (@RequestBody UsuarioDTO usuarioDTO, BindingResult result) {
        Reposta<UsuarioRespostaDTO> reposta = new Reposta<>();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error
                    -> reposta.adicionarMensagemErro(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(reposta);
        }
        try {
            Optional<Usuario> usuario = usuarioServico.atualizar(usuarioDTO);
            if (!usuario.isPresent()){
                reposta.setData(new UsuarioRespostaDTO());
                new ResponseEntity<>(reposta, HttpStatus.BAD_REQUEST);
            }

            UsuarioRespostaDTO repostaDTO = UsuarioRespostaDTO.converterEntidadeParaDTO(usuario.get());
            reposta.setData(repostaDTO);
            return new ResponseEntity<>(reposta, HttpStatus.OK);
        }catch (CargoNaoEncontradoException naoEncontrado){
            reposta.adicionarMensagemErro(naoEncontrado.getMessage());
            reposta.setData(new UsuarioRespostaDTO());
            return new ResponseEntity<>(reposta, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            reposta.adicionarMensagemErro(e.getMessage());
            reposta.setData(new UsuarioRespostaDTO());
            return new ResponseEntity<>(reposta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = USUARIO_POR_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reposta<UsuarioRespostaDTO>> obterPorId(@PathVariable("id") long id) {
        Reposta<UsuarioRespostaDTO> reposta = new Reposta<>();
        try {
            Optional<Usuario> usuario = usuarioServico.obterPorId(id);
            if (!usuario.isPresent()){
                reposta.setData(new UsuarioRespostaDTO());
                new ResponseEntity<>(reposta, HttpStatus.BAD_REQUEST);
            }

            UsuarioRespostaDTO responseDTO = UsuarioRespostaDTO.converterEntidadeParaDTO(usuario.get());
            reposta.setData(responseDTO);
            return new ResponseEntity<>(reposta, HttpStatus.OK);
        }catch (UsuarioNaoEncontradoException naoEncontrado){
            reposta.adicionarMensagemErro(naoEncontrado.getMessage());
            return new ResponseEntity<>(reposta, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            reposta.adicionarMensagemErro(e.getMessage());
            reposta.setData(new UsuarioRespostaDTO());
            return new ResponseEntity<>(reposta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = USUARIO_POR_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reposta<UsuarioRespostaDTO>> obterPorLogin(@RequestParam("login") String login) {
        Reposta<UsuarioRespostaDTO> reposta = new Reposta<>();
        try {
            Optional<Usuario> usuario = usuarioServico.obterPorLogin(login);
            if (!usuario.isPresent()){
                reposta.setData(new UsuarioRespostaDTO());
                new ResponseEntity<>(reposta, HttpStatus.BAD_REQUEST);
            }

            UsuarioRespostaDTO responseDTO = UsuarioRespostaDTO.converterEntidadeParaDTO(usuario.get());
            reposta.setData(responseDTO);
            return new ResponseEntity<>(reposta, HttpStatus.OK);
        }catch (UsuarioNaoEncontradoException naoEncontrado){
            reposta.adicionarMensagemErro(naoEncontrado.getMessage());
            return new ResponseEntity<>(reposta, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            reposta.adicionarMensagemErro(e.getMessage());
            reposta.setData(new UsuarioRespostaDTO());
            return new ResponseEntity<>(reposta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = USUARIO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reposta<List<UsuarioRespostaDTO>>> obterTodos() {
        Reposta<List<UsuarioRespostaDTO>> reposta = new Reposta<>();
        try {
            List<Usuario> usuarios = usuarioServico.obterTodos();

            List<UsuarioRespostaDTO> responseDTO;
            if(!usuarios.isEmpty()){
                responseDTO = usuarios.stream()
                        .map(usuario -> UsuarioRespostaDTO.converterEntidadeParaDTO(usuario))
                        .collect(Collectors.toList());
            }
            else
                responseDTO = Collections.EMPTY_LIST;

            reposta.setData(responseDTO);
            return new ResponseEntity<>(reposta, HttpStatus.OK);
        }catch (UsuarioNaoEncontradoException naoEncontrado){
            reposta.adicionarMensagemErro(naoEncontrado.getMessage());
            return new ResponseEntity<>(reposta, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            reposta.adicionarMensagemErro(e.getMessage());
            return new ResponseEntity<>(reposta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = USUARIO_POR_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reposta<Boolean>> delete(@PathVariable("id") long id) {
        Reposta<Boolean> reposta = new Reposta<>();
        try {
            usuarioServico.delete(id);
            reposta.setData(true);
            return new ResponseEntity<>(reposta, HttpStatus.NO_CONTENT);
        }catch (UsuarioNaoEncontradoException naoEncontrado){
            reposta.adicionarMensagemErro(naoEncontrado.getMessage());
            return new ResponseEntity<>(reposta, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            reposta.adicionarMensagemErro(e.getMessage());
            return new ResponseEntity<>(reposta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
