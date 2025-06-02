package GestionUsuarios.GestionUsuarios.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoVentasRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe ser una dirección de correo electrónico con formato válido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    @NotBlank(message = "La fecha de nacimiento no puede estar vacía")
    private String fechaNacimiento;

    @NotBlank(message = "El RUT no puede estar vacío")
    private String rut;

    @NotNull(message = "El ID del tipo de usuario no puede ser nulo")
    private Long tipoUsuarioId;

    @NotBlank(message = "La fecha de contratación no puede estar vacía")
    private String fechaContratacion; // Considerar LocalDate

    @NotNull(message = "El salario no puede ser nulo")
    @Positive(message = "El salario debe ser un valor positivo")
    private Double salario;
}