package com.multiclinicas.api.repositories;

import com.multiclinicas.api.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Busca todos os pacientes de uma clínica específica
    List<Paciente> findByClinicaId(Long clinicId);

    // Busca um paciente específico garantindo que pertence à clínica
    Optional<Paciente> findByIdAndClinicaId(Long id, Long clinicId);

    // Verifica se já existe um CPF cadastrado naquela clínica (Regra de Negócio)
    boolean existsByCpfAndClinicaId(String cpf, Long clinicId);
}
