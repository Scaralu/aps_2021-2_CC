package com.scarano.dev.auth.model.entidade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter

public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IdCargo", nullable=false)
    private long id;
    @Column(name="Descricao", nullable=false)
    private String descricao;
    @ManyToOne()
    @JoinColumn(name = "NivelId", referencedColumnName = "IdNivel")
    private Nivel nivel;
}
