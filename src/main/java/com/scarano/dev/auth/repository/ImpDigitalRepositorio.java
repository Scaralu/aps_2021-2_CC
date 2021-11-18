package com.scarano.dev.auth.repository;

import com.scarano.dev.auth.model.entidade.ImpressaoDigital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImpDigitalRepositorio  extends JpaRepository<ImpressaoDigital, Long> {
    @Query("SELECT i FROM ImpressaoDigital i WHERE i.usuario.id = :usuarioId and i.usuario.login = :login")
    Optional<ImpressaoDigital> findByUsuarioIdELogin(@Param("usuarioId") long usuarioId, @Param("login") String login);
}
