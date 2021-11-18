package com.scarano.dev.auth.repository;

import com.scarano.dev.auth.model.entidade.Informacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InformacaoRepositorio extends JpaRepository<Informacao, Long> {
    @Query("SELECT i FROM Informacao i WHERE i.nivel.id <= :level_ID")
    Optional<List<Informacao>> findByNivel(@Param("level_ID") long level_ID);
}
