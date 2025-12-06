package com.multiclinicas.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multiclinicas.api.mappers.PacienteMapper;
import com.multiclinicas.api.models.Paciente;
import com.multiclinicas.api.services.ClinicaService;
import com.multiclinicas.api.services.PacienteService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.multiclinicas.api.dtos.PacienteCreateDTO;
import com.multiclinicas.api.dtos.PacienteDTO;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;
    private final PacienteMapper pacienteMapper;
    private final ClinicaService clinicaService;

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> getAllPacientes() {
        List<Paciente> pacientes = pacienteService.findAll();
        List<PacienteDTO> dto = pacientes.stream().map(pacienteMapper::toDto).toList();
        return ResponseEntity.ok(dto);

    }

    @PostMapping
    public ResponseEntity<PacienteDTO> createPaciente(
            @RequestHeader("X-Clinic-ID") Long clinicaId,
            @RequestBody PacienteCreateDTO dto) {
        Paciente paciente = pacienteMapper.toEntity(dto);
        paciente.setClinica(clinicaService.findById(clinicaId));

        if (dto.senhaHash() != null) {
            paciente.setSenhaHash(dto.senhaHash());
        } else {
            throw new IllegalArgumentException("Senha é obrigatória");
        }

        Paciente salvo = pacienteService.create(paciente);

        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteMapper.toDto(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> getPacienteById(@PathVariable Long id) {
        Paciente paciente = pacienteService.findById(id);
        return ResponseEntity.ok(pacienteMapper.toDto(paciente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> updatePaciente(
            @PathVariable Long id,
            @RequestBody PacienteCreateDTO dto) {

        Paciente novosDados = pacienteMapper.toEntity(dto);

        if (dto.senhaHash() != null) {
            novosDados.setSenhaHash(dto.senhaHash());
        }

        Paciente pacienteAtualizado = pacienteService.update(id, novosDados);

        return ResponseEntity.ok(pacienteMapper.toDto(pacienteAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
