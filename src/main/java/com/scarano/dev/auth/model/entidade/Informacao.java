package com.scarano.dev.auth.model.entidade;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "novo")
@Getter
public class Informacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IdInformacao", nullable=false)
    private long id;
    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "NivelId", referencedColumnName = "IdNivel")
    private Nivel nivel;
    @NonNull
    @Column(name="Descricao", nullable=false)
    private String descricao;
    @NonNull
    @Column(name="RiscoSubterraneoGus", nullable=false)
    private String riscoSubterraneoGus;
    @NonNull
    @Column(name="RiscoSubterraneoEpa", nullable=false)
    private String riscoSubterraneoEpa;
    @NonNull
    @Column(name="RiscoSuperficialGossSedimental", nullable=false)
    private String riscoSuperficialGossSedimental;
    @NonNull
    @Column(name="RiscoSuperficialGossDissolvido", nullable=false)
    private String riscoSuperficialGossDissolvido;
    @NonNull
    @Column(name="Endereco", nullable=false)
    private String endereco;
}
