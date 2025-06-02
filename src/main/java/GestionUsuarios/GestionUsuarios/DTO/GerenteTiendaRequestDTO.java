package GestionUsuarios.GestionUsuarios.DTO; // Corrected package

// import jakarta.validation.constraints.*; // Already imported via individual imports below
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GerenteTiendaRequestDTO {
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

    @NotNull(message = "Los años de experiencia no pueden ser nulos")
    @Min(value = 0, message = "Los años de experiencia no pueden ser negativos")
    private Integer anosExperiencia;

    @NotBlank(message = "La tienda asignada no puede estar vacía")
    @Size(max = 100, message = "El nombre de la tienda asignada no puede exceder los 100 caracteres")
    private String tiendaAsignada;
}