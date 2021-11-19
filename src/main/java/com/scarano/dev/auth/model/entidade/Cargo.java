package com.scarano.dev.auth.model.entidade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setterls
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable=false)
    private long id;
    @Column(name="description", nullable=false)
    private String description;
    @ManyToOne()
    @JoinColumn(name = "level_ID", referencedColumnName = "id")
    private Nivel nivel;
}
