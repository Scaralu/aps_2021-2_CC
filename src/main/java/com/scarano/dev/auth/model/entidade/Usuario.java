package com.scarano.dev.auth.model.entidade;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "novo")
@Getter @Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IdUsuario", nullable=false)
    private long id;
    @Column(name="Nome", nullable=false)
    @NonNull
    private String nome;
    @Column(name="Sobrenome", nullable=false)
    @NonNull
    private String sobrenome;
    @ManyToOne()
    @NonNull
    private Cargo cargo;
    @NonNull
    @Column(name="Login", nullable=false)
    private String login;
    @NonNull
    @Column(name="Senha", nullable=false)
    private String senha;
}
