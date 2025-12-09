package com.multiclinicas.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.multiclinicas.api.repositories.PacienteRepository;

import jakarta.transaction.Transactional;

import com.multiclinicas.api.exceptions.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.multiclinicas.api.models.Paciente;

@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;

    public PacienteServiceImpl(PacienteRepository pacienteRepository, PasswordEncoder passwordEncoder) {
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
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
        if (paciente.getSenhaHash() != null) {
            String hash = passwordEncoder.encode(paciente.getSenhaHash());
            paciente.setSenhaHash(hash);
        }
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
        pacienteAntigo.setNome(paciente.getNome());
        pacienteAntigo.setEndereco(paciente.getEndereco());

        if (paciente.getSenhaHash() != null && !paciente.getSenhaHash().isBlank()) {
            String hash = passwordEncoder.encode(paciente.getSenhaHash());
            pacienteAntigo.setSenhaHash(hash);
        }
        return pacienteAntigo;

    }

}
