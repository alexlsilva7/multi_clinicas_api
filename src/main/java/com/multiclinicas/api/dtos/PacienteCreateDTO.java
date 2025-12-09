package com.multiclinicas.api.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record PacienteCreateDTO(
                @NotBlank(message = "O nome é obrigatório") String nome,

                @NotBlank(message = "O CPF é obrigatório") @CPF(message = "CPF inválido") String cpf,

                String telefone,

                @Email(message = "Email inválido") String email,

                @Valid EnderecoDTO endereco) {
}
