package GestionUsuarios.GestionUsuarios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
// Lombok AllArgsConstructor podría necesitar ser explícito si la clase base tiene campos
// import lombok.AllArgsConstructor;

@Entity
@Table(name = "administradores")
@Data
@EqualsAndHashCode(callSuper = true) // Importante para Lombok con herencia
@NoArgsConstructor
// @AllArgsConstructor // Si es necesario
public class Administrador extends Usuario {
    // Campos específicos del administrador, si los hubiera, irían aquí.
    // Basado en los DTOs, no hay campos adicionales específicos para Administrador.
    //
}