package com.multiclinicas.api.mappers;

import com.multiclinicas.api.dtos.EnderecoDTO;
import com.multiclinicas.api.dtos.PacienteCreateDTO;
import com.multiclinicas.api.dtos.PacienteDTO;
import com.multiclinicas.api.models.Endereco;
import com.multiclinicas.api.models.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public PacienteDTO toDTO(Paciente paciente) {
        if (paciente == null)
            return null;

        EnderecoDTO enderecoDTO = null;
        if (paciente.getEndereco() != null) {
            Endereco e = paciente.getEndereco();
            enderecoDTO = new EnderecoDTO(e.getCep(), e.getLogradouro(), e.getNumero(),
                    e.getComplemento(), e.getBairro(), e.getCidade(), e.getEstado());
        }

        return new PacienteDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getEmail(),
                paciente.getTelefone(),
                enderecoDTO);
    }

    public Paciente toEntity(PacienteCreateDTO dto) {
        if (dto == null)
            return null;

        Paciente paciente = new Paciente();
        paciente.setNome(dto.nome());
        paciente.setCpf(dto.cpf());
        paciente.setTelefone(dto.telefone());
        paciente.setEmail(dto.email());

        if (dto.endereco() != null) {
            Endereco endereco = new Endereco();
            EnderecoDTO endDto = dto.endereco();
            endereco.setCep(endDto.cep());
            endereco.setLogradouro(endDto.logradouro());
            endereco.setNumero(endDto.numero());
            endereco.setComplemento(endDto.complemento());
            endereco.setBairro(endDto.bairro());
            endereco.setCidade(endDto.cidade());
            endereco.setEstado(endDto.estado());
            paciente.setEndereco(endereco);
        }

        return paciente;
    }
}
