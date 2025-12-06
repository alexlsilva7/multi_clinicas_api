package com.multiclinicas.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.multiclinicas.api.models.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

}
