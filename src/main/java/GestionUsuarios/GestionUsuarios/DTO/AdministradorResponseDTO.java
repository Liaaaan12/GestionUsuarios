package GestionUsuarios.GestionUsuarios.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdministradorResponseDTO extends RepresentationModel<AdministradorResponseDTO> {
    private Long id;
    private String nombre;
    private String email;
    private String fechaNacimiento;
    private String rut;
    private TipoUsuarioResponseDTO tipoUsuario;
}