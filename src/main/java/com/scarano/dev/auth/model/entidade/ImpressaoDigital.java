package com.scarano.dev.auth.model.entidade;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor()
@RequiredArgsConstructor(staticName = "novo")
@Getter @Setter
public class ImpressaoDigital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @NonNull
    @Column(name = "name", nullable = false)
    private String name;
    @NonNull
    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;
    @NonNull
    @OneToOne(optional = false)
    @JoinColumn(name="user_ID", referencedColumnName = "id")
    private Usuario usuario;
}
