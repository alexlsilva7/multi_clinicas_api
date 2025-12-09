package com.multiclinicas.api.controllers;

import com.multiclinicas.api.config.tenant.TenantContext;
import com.multiclinicas.api.dtos.PacienteCreateDTO;
import com.multiclinicas.api.dtos.PacienteDTO;
import com.multiclinicas.api.mappers.PacienteMapper;
import com.multiclinicas.api.models.Paciente;
import com.multiclinicas.api.services.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Gerenciamento de pacientes")
public class PacienteController {

    private final PacienteService pacienteService;
    private final PacienteMapper pacienteMapper;

    @GetMapping
    @Operation(summary = "Listar pacientes", description = "Lista todos os pacientes da clínica logada")
    public ResponseEntity<List<PacienteDTO>> findAll() {
        Long clinicId = TenantContext.getClinicId();
        List<Paciente> pacientes = pacienteService.findAllByClinicId(clinicId);
        return ResponseEntity.ok(pacientes.stream()
                .map(pacienteMapper::toDTO)
                .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente", description = "Busca um paciente pelo ID dentro da clínica logada")
    public ResponseEntity<PacienteDTO> findById(@PathVariable Long id) {
        Long clinicId = TenantContext.getClinicId();
        Paciente paciente = pacienteService.findByIdAndClinicId(id, clinicId);
        return ResponseEntity.ok(pacienteMapper.toDTO(paciente));
    }

    @PostMapping
    @Operation(summary = "Cadastrar paciente", description = "Cadastra um novo paciente na clínica logada")
    public ResponseEntity<PacienteDTO> create(@RequestBody @Valid PacienteCreateDTO dto) {
        Long clinicId = TenantContext.getClinicId();
        Paciente paciente = pacienteMapper.toEntity(dto);
        Paciente saved = pacienteService.create(clinicId, paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar paciente", description = "Atualiza dados de um paciente existente")
    public ResponseEntity<PacienteDTO> update(@PathVariable Long id, @RequestBody @Valid PacienteCreateDTO dto) {
        Long clinicId = TenantContext.getClinicId();
        Paciente paciente = pacienteMapper.toEntity(dto);
        Paciente updated = pacienteService.update(id, clinicId, paciente);
        return ResponseEntity.ok(pacienteMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover paciente", description = "Remove um paciente da base de dados")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Long clinicId = TenantContext.getClinicId();
        pacienteService.delete(id, clinicId);
        return ResponseEntity.noContent().build();
    }
}
