package com.scarano.dev.auth.repository;

import com.scarano.dev.auth.model.entidade.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepositorio extends JpaRepository<Cargo, Long> {
}
