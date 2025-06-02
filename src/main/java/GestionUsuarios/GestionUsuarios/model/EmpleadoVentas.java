package GestionUsuarios.GestionUsuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor;

@Entity
@Table(name = "empleados_ventas")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
// @AllArgsConstructor // Si es necesario
public class EmpleadoVentas extends Usuario {

    @Column(name = "fecha_contratacion")
    private String fechaContratacion; // Considerar java.time.LocalDate

    @Column(nullable = false)
    private Double salario; //
}