package com.scarano.dev.auth.repository;

import com.scarano.dev.auth.model.entidade.ImpressaoDigital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImpDigitalRepositorio  extends JpaRepository<ImpressaoDigital, Long> {
    @Query("SELECT i FROM ImpressaoDigital i WHERE i.usuario.id = :usuario_ID and i.usuario.login = :username")
    Optional<ImpressaoDigital> findByUsuarioIdELogin(
        @Param("usuario_ID")
        long usuario_ID,

        @Param("username")
        String username
    );
}
