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
    @Column(name = "IdDigital")
    private long id;
    @NonNull
    @Column(name = "Nome", nullable = false)
    private String nome;
    @NonNull
    @Column(name = "TipoArquivo", nullable = false)
    private String tipoArquivo;
    @NonNull
    @Lob
    @Column(name = "Conteudo", nullable = false)
    private byte[] conteudo;
    @NonNull
    @OneToOne(optional = false)
    @JoinColumn(name = "Usuario_Id")
    private Usuario usuario;
}
