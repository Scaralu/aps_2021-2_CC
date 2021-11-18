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
    @Column(name="id", nullable=false)
    private long id;
    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "level_ID", referencedColumnName = "id")
    private Nivel nivel;
    @NonNull
    @Column(name="description", nullable=false)
    private String description;
}
