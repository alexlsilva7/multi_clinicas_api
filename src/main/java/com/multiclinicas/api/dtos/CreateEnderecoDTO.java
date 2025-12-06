package com.multiclinicas.api.dtos;

public record CreateEnderecoDTO(String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String pais) {

}
