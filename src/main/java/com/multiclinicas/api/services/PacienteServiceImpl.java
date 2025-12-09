package com.multiclinicas.api.services;

import com.multiclinicas.api.exceptions.ResourceConflictException;
import com.multiclinicas.api.exceptions.ResourceNotFoundException;
import com.multiclinicas.api.models.Clinica;
import com.multiclinicas.api.models.Paciente;
import com.multiclinicas.api.repositories.ClinicaRepository;
import com.multiclinicas.api.repositories.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final ClinicaRepository clinicaRepository;

    @Override
    public List<Paciente> findAllByClinicId(Long clinicId) {
        return pacienteRepository.findByClinicaId(clinicId);
    }

    @Override
    public Paciente findByIdAndClinicId(Long id, Long clinicId) {
        return pacienteRepository.findByIdAndClinicaId(id, clinicId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
    }

    @Override
    @Transactional
    public Paciente create(Long clinicId, Paciente paciente) {
        Clinica clinica = clinicaRepository.findById(clinicId)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica não encontrada"));

        // Regra: CPF único por clínica
        if (pacienteRepository.existsByCpfAndClinicaId(paciente.getCpf(), clinicId)) {
            throw new ResourceConflictException("Já existe um paciente com este CPF nesta clínica.");
        }

        paciente.setClinica(clinica);
        return pacienteRepository.save(paciente);
    }

    @Override
    @Transactional
    public Paciente update(Long id, Long clinicId, Paciente dadosAtualizados) {
        Paciente pacienteExistente = findByIdAndClinicId(id, clinicId);

        // Se mudou o CPF, verifica duplicidade
        if (!pacienteExistente.getCpf().equals(dadosAtualizados.getCpf()) &&
                pacienteRepository.existsByCpfAndClinicaId(dadosAtualizados.getCpf(), clinicId)) {
            throw new ResourceConflictException("CPF já cadastrado para outro paciente nesta clínica.");
        }

        pacienteExistente.setNome(dadosAtualizados.getNome());
        pacienteExistente.setCpf(dadosAtualizados.getCpf());
        pacienteExistente.setEmail(dadosAtualizados.getEmail());
        pacienteExistente.setTelefone(dadosAtualizados.getTelefone());

        // Atualizar endereço se fornecido (simplificado)
        if (dadosAtualizados.getEndereco() != null) {
            pacienteExistente.setEndereco(dadosAtualizados.getEndereco());
        }

        return pacienteRepository.save(pacienteExistente);
    }

    @Override
    @Transactional
    public void delete(Long id, Long clinicId) {
        Paciente paciente = findByIdAndClinicId(id, clinicId);
        pacienteRepository.delete(paciente);
    }
}
