package com.multiclinicas.api.dtos;

public record PacienteCreateDTO(
                String nome,
                String email,
                String cpf,
                String telefone,
                String telefoneSecundario,
                String senhaHash,
                CreateEnderecoDTO endereco) {
}
