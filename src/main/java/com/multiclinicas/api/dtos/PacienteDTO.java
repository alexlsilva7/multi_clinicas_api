package com.multiclinicas.api.dtos;

public record PacienteDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        EnderecoDTO endereco) {
}
