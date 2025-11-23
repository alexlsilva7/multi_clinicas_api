package com.multiclinicas.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
// Garante que não haja pacientes com CPF duplicado na mesma clínica
@Table(name = "pacientes", uniqueConstraints = @UniqueConstraint(columnNames = { "clinic_id", "cpf" }))
public class Paciente extends BaseUsuario {

    private String email;

    private String senhaHash;
}