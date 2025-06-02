package GestionUsuarios.GestionUsuarios.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuarioRequestDTO {
    @NotBlank(message = "El nombre del tipo de usuario no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre del tipo de usuario debe tener entre 3 y 50 caracteres")
    private String nombre;
}