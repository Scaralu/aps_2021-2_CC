package com.scarano.dev.auth.controller;

import com.scarano.dev.auth.model.Reposta;
import com.scarano.dev.auth.model.dtos.ImpressaoDigitalRespostaDTO;
import com.scarano.dev.auth.model.excecoes.UsuarioNaoEncontradoException;
import com.scarano.dev.auth.model.entidade.ImpressaoDigital;
import com.scarano.dev.auth.service.ImpressaoDigitalServico;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class ImpressaoDigitalController {
    private final ImpressaoDigitalServico impressaoDigitalServico;
    private final static String IMPRESSAO_DIGITAL = "/impressaoDigital";
    private final static String USUARIO_ID = "usuarioId";
    private final static String ARQUIVO = "arquivo";
    private final static String SALVAR_DIGITAL = IMPRESSAO_DIGITAL+"/{usuarioId}/arquivo";

    public ImpressaoDigitalController(ImpressaoDigitalServico impressaoDigitalServico) {
        this.impressaoDigitalServico = impressaoDigitalServico;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = SALVAR_DIGITAL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reposta<ImpressaoDigitalRespostaDTO>> salvar(@RequestParam(ARQUIVO) MultipartFile arquivo, @PathVariable(USUARIO_ID) long usuarioId) {
        Reposta<ImpressaoDigitalRespostaDTO> resposta = new Reposta<>();
        try {
            Optional<ImpressaoDigital> impressaoDigital = impressaoDigitalServico.salvar(arquivo, usuarioId);
            if (!impressaoDigital.isPresent()){
                resposta.setData(new ImpressaoDigitalRespostaDTO());
                new ResponseEntity<>(resposta, HttpStatus.BAD_REQUEST);
            }
            ImpressaoDigitalRespostaDTO repostaDTO = ImpressaoDigitalRespostaDTO.converterEntidadeParaDTO(impressaoDigital.get());
            resposta.setData(repostaDTO);
            return new ResponseEntity<>(resposta, HttpStatus.CREATED);
        }catch (UsuarioNaoEncontradoException naoEncontrado){
            resposta.adicionarMensagemErro(naoEncontrado.getMessage());
            resposta.setData(new ImpressaoDigitalRespostaDTO());
            return new ResponseEntity<>(resposta, HttpStatus.NOT_FOUND);
        }catch (IOException erroArquivo) {
            resposta.adicionarMensagemErro(erroArquivo.getMessage());
            resposta.setData(new ImpressaoDigitalRespostaDTO());
            return new ResponseEntity<>(resposta, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            resposta.adicionarMensagemErro(e.getMessage());
            resposta.setData(new ImpressaoDigitalRespostaDTO());
            return new ResponseEntity<>(resposta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(value = SALVAR_DIGITAL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reposta<ImpressaoDigitalRespostaDTO>> atualizar (@RequestParam(ARQUIVO) MultipartFile arquivo, @PathVariable(USUARIO_ID) long usuarioId) {
        Reposta<ImpressaoDigitalRespostaDTO> resposta = new Reposta<>();
        try {
            Optional<ImpressaoDigital> impressaoDigital = impressaoDigitalServico.atualizar(arquivo, usuarioId);
            if (!impressaoDigital.isPresent()){
                resposta.setData(new ImpressaoDigitalRespostaDTO());
                new ResponseEntity<>(resposta, HttpStatus.BAD_REQUEST);
            }
            ImpressaoDigitalRespostaDTO repostaDTO = ImpressaoDigitalRespostaDTO.converterEntidadeParaDTO(impressaoDigital.get());
            resposta.setData(repostaDTO);
            return new ResponseEntity<>(resposta, HttpStatus.OK);
        }catch (UsuarioNaoEncontradoException naoEncontrado){
            resposta.adicionarMensagemErro(naoEncontrado.getMessage());
            resposta.setData(new ImpressaoDigitalRespostaDTO());
            return new ResponseEntity<>(resposta, HttpStatus.NOT_FOUND);
        }catch (IOException erroArquivo){
            resposta.adicionarMensagemErro(erroArquivo.getMessage());
            resposta.setData(new ImpressaoDigitalRespostaDTO());
            return new ResponseEntity<>(resposta, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            resposta.adicionarMensagemErro(e.getMessage());
            resposta.setData(new ImpressaoDigitalRespostaDTO());
            return new ResponseEntity<>(resposta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
