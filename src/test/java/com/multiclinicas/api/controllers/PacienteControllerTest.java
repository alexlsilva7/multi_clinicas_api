package com.multiclinicas.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multiclinicas.api.config.WebConfig;
import com.multiclinicas.api.config.tenant.TenantInterceptor;
import com.multiclinicas.api.dtos.PacienteCreateDTO;
import com.multiclinicas.api.dtos.PacienteDTO;
import com.multiclinicas.api.mappers.PacienteMapper;
import com.multiclinicas.api.models.Paciente;
import com.multiclinicas.api.repositories.ClinicaRepository;
import com.multiclinicas.api.services.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PacienteController.class)
@Import({ WebConfig.class, TenantInterceptor.class })
class PacienteControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private PacienteService pacienteService;

        @MockitoBean
        private PacienteMapper pacienteMapper;

        @MockitoBean
        private ClinicaRepository clinicaRepository;

        private final Long CLINIC_ID = 1L;

        @BeforeEach
        void setup() {
                // Simula que a clínica do header existe para passar pelo Interceptor
                when(clinicaRepository.existsById(CLINIC_ID)).thenReturn(true);
        }

        @Test
        @DisplayName("Deve criar paciente com sucesso")
        void shouldCreatePaciente() throws Exception {
                String validCpf = "42139076001";
                PacienteCreateDTO createDTO = new PacienteCreateDTO(
                                "Maria Souza",
                                validCpf,
                                "11999999999",
                                "maria@email.com",
                                null);

                Paciente paciente = new Paciente();
                paciente.setId(1L);

                PacienteDTO responseDTO = new PacienteDTO(
                                1L, "Maria Souza", validCpf, "maria@email.com", "11999999999", null);

                when(pacienteMapper.toEntity(any(PacienteCreateDTO.class))).thenReturn(paciente);
                when(pacienteService.create(eq(CLINIC_ID), any(Paciente.class))).thenReturn(paciente);
                when(pacienteMapper.toDTO(paciente)).thenReturn(responseDTO);

                mockMvc.perform(post("/pacientes")
                                .header("X-Clinic-ID", CLINIC_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.nome").value("Maria Souza"));
        }

        @Test
        @DisplayName("Deve retornar 400 se CPF for inválido")
        void shouldReturn400WhenCpfIsInvalid() throws Exception {
                // CPF inválido propositalmente
                PacienteCreateDTO invalidDTO = new PacienteCreateDTO(
                                "Teste", "123", "11999999999", "teste@email.com", null);

                mockMvc.perform(post("/pacientes")
                                .header("X-Clinic-ID", CLINIC_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.details.cpf").exists());
        }

        @Test
        @DisplayName("Deve retornar 400 se Header da clínica estiver faltando")
        void shouldReturn400WhenHeaderMissing() throws Exception {
                PacienteCreateDTO dto = new PacienteCreateDTO(
                                "Maria", "12345678900", "11999999999", "maria@email.com", null);

                mockMvc.perform(post("/pacientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve buscar paciente por ID")
        void shouldGetPacienteById() throws Exception {
                Long pacienteId = 1L;
                Paciente paciente = new Paciente();
                paciente.setId(pacienteId);
                PacienteDTO responseDTO = new PacienteDTO(
                                pacienteId, "João", "12345678900", null, null, null);

                when(pacienteService.findByIdAndClinicId(pacienteId, CLINIC_ID)).thenReturn(paciente);
                when(pacienteMapper.toDTO(paciente)).thenReturn(responseDTO);

                mockMvc.perform(get("/pacientes/{id}", pacienteId)
                                .header("X-Clinic-ID", CLINIC_ID))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(pacienteId));
        }
}
