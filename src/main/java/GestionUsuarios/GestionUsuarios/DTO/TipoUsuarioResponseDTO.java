package GestionUsuarios.GestionUsuarios.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuarioResponseDTO {
    private Long id;
    private String nombre;
}