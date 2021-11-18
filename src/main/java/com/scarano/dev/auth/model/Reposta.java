package com.scarano.dev.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reposta<T> {

    private T data;
    private Object error;

    public void adicionarMensagemErro(String msgError) {
        RespostaComErro error = new RespostaComErro()
                .setMessage(msgError)
                .setTimestamp(LocalDateTime.now());
        setError(error);
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @NoArgsConstructor
    public class RespostaComErro {

        @NotNull(message="Horário não pode ser nulo")
        private LocalDateTime timestamp;

        @NotNull(message="Detalhes não pode ser nulo")
        private String message;
    }
}
