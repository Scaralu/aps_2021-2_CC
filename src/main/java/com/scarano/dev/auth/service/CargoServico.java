package com.scarano.dev.auth.service;

import com.scarano.dev.auth.model.entidade.Cargo;
import com.scarano.dev.auth.repository.CargoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoServico {
    @Autowired
    CargoRepositorio cargoRepositorio;

    public List<Cargo> obterTodos() {
        return cargoRepositorio.findAll();
    }
}
