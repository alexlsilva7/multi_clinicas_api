package com.multiclinicas.api.services;

import java.util.List;

import com.multiclinicas.api.models.Paciente;

public interface PacienteService {
    List<Paciente> findAll();

    Paciente findById(Long id);

    Paciente create(Paciente paciente);

    void delete(Long id);

    Paciente update(Long id, Paciente paciente);
}
