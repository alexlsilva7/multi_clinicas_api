package com.multiclinicas.api.models;

import com.multiclinicas.api.models.enums.Role;
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
@Table(name = "usuarios_admin", uniqueConstraints = @UniqueConstraint(columnNames = { "clinic_id", "email" }))
public class UsuarioAdmin extends BaseUsuario {

    private String email;

    private String senhaHash;

    @Enumerated(EnumType.STRING)
    private Role role;
}