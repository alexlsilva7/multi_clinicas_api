package com.multiclinicas.api.services;

import com.multiclinicas.api.exceptions.ResourceConflictException;

import com.multiclinicas.api.models.Clinica;

import com.multiclinicas.api.models.Paciente;
import com.multiclinicas.api.repositories.ClinicaRepository;
import com.multiclinicas.api.repositories.PacienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ClinicaRepository clinicaRepository;

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    @Test
    @DisplayName("Deve criar paciente com sucesso")
    void shouldCreatePacienteSuccessfully() {
        // Given
        Long clinicId = 1L;
        Clinica clinica = new Clinica();
        clinica.setId(clinicId);

        Paciente novoPaciente = new Paciente();
        novoPaciente.setCpf("42139076001");
        novoPaciente.setNome("João Silva");

        when(clinicaRepository.findById(clinicId)).thenReturn(Optional.of(clinica));
        when(pacienteRepository.existsByCpfAndClinicaId("42139076001", clinicId)).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Paciente result = pacienteService.create(clinicId, novoPaciente);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getClinica()).isEqualTo(clinica);
        verify(pacienteRepository).save(novoPaciente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar paciente com CPF duplicado na mesma clínica")
    void shouldThrowExceptionWhenCpfExistsInClinic() {
        // Given
        Long clinicId = 1L;
        Clinica clinica = new Clinica();
        clinica.setId(clinicId);

        Paciente novoPaciente = new Paciente();
        novoPaciente.setCpf("12345678900");

        when(clinicaRepository.findById(clinicId)).thenReturn(Optional.of(clinica));
        when(pacienteRepository.existsByCpfAndClinicaId("12345678900", clinicId)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> pacienteService.create(clinicId, novoPaciente))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("Já existe um paciente com este CPF");

        verify(pacienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar paciente com sucesso")
    void shouldUpdatePacienteSuccessfully() {
        // Given
        Long clinicId = 1L;
        Long pacienteId = 10L;

        Paciente pacienteExistente = new Paciente();
        pacienteExistente.setId(pacienteId);
        pacienteExistente.setCpf("11111111111");
        pacienteExistente.setNome("Antigo");

        Paciente dadosAtualizados = new Paciente();
        dadosAtualizados.setCpf("11111111111");
        dadosAtualizados.setNome("Novo Nome");

        when(pacienteRepository.findByIdAndClinicaId(pacienteId, clinicId)).thenReturn(Optional.of(pacienteExistente));
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Paciente result = pacienteService.update(pacienteId, clinicId, dadosAtualizados);

        // Then
        assertThat(result.getNome()).isEqualTo("Novo Nome");
        verify(pacienteRepository).save(pacienteExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar usar CPF de outro paciente na atualização")
    void shouldThrowExceptionWhenUpdatingToExistingCpf() {
        // Given
        Long clinicId = 1L;
        Long pacienteId = 10L;

        Paciente pacienteExistente = new Paciente();
        pacienteExistente.setId(pacienteId);
        pacienteExistente.setCpf("11111111111");

        Paciente dadosAtualizados = new Paciente();
        dadosAtualizados.setCpf("22222222222"); // CPF novo que já existe no banco

        when(pacienteRepository.findByIdAndClinicaId(pacienteId, clinicId)).thenReturn(Optional.of(pacienteExistente));
        when(pacienteRepository.existsByCpfAndClinicaId("22222222222", clinicId)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> pacienteService.update(pacienteId, clinicId, dadosAtualizados))
                .isInstanceOf(ResourceConflictException.class);
    }
}
