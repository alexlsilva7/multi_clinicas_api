package com.multiclinicas.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
// Garante que não haja especialidades com o mesmo nome na mesma clínica
@Table(name = "especialidades", uniqueConstraints = @UniqueConstraint(columnNames = { "clinic_id", "nome" }))
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinica clinica;

    private String nome;
}