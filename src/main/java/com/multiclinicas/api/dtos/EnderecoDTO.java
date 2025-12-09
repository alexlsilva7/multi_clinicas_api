package com.multiclinicas.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoDTO(
                @NotBlank(message = "O CEP é obrigatório") String cep,

                @NotBlank(message = "O logradouro é obrigatório") String logradouro,

                @NotBlank(message = "O número é obrigatório") String numero,

                String complemento,

                @NotBlank(message = "O bairro é obrigatório") String bairro,

                @NotBlank(message = "A cidade é obrigatória") String cidade,

                @NotBlank(message = "O estado é obrigatório") @Size(min = 2, max = 2, message = "O estado deve ser a sigla (UF) com 2 caracteres") String estado) {
}
