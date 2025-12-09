package com.multiclinicas.api.services;

import com.multiclinicas.api.models.Paciente;
import java.util.List;

public interface PacienteService {
    List<Paciente> findAllByClinicId(Long clinicId);

    Paciente findByIdAndClinicId(Long id, Long clinicId);

    Paciente create(Long clinicId, Paciente paciente);

    Paciente update(Long id, Long clinicId, Paciente paciente);

    void delete(Long id, Long clinicId);
}
