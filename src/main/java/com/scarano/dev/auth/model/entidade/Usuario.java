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
    @Column(name="id", nullable=false)
    private long id;
    @Column(name="name", nullable=false)
    @NonNull
    private String name;
    @Column(name="last name", nullable=false)
    @NonNull
    private String last_name;
    @NonNull
    @ManyToOne()
    @JoinColumn(name="role_ID", referencedColumnName = "id")
    private Cargo cargo;
    @NonNull
    @Column(name="username", nullable=false)
    private String username;
    @NonNull
    @Column(name="password", nullable=false)
    private String password;
}
