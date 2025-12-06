package com.multiclinicas.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.multiclinicas.api.repositories.PacienteRepository;

import jakarta.transaction.Transactional;

import com.multiclinicas.api.exceptions.ResourceNotFoundException;
import com.multiclinicas.api.models.Paciente;

@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }

    @Override
    public Paciente findById(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não foi possível encontrar paciente com Id: " + id));
    }

    @Override
    @Transactional
    public Paciente create(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("O paciente com Id " + id + " não existe");
        }
        pacienteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Paciente update(Long id, Paciente paciente) {
        Paciente pacienteAntigo = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não foi possível encontrar paciente com id: " + id));
        pacienteAntigo.setEmail(paciente.getEmail());
        pacienteAntigo.setSenhaHash(paciente.getSenhaHash());
        pacienteAntigo.setNome(paciente.getNome());
        pacienteAntigo.setEndereco(paciente.getEndereco());
        return pacienteAntigo;

    }

}
