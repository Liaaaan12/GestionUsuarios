package GestionUsuarios.GestionUsuarios.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClienteResponseDTO extends RepresentationModel<ClienteResponseDTO> {
    private Long id;
    private String nombre;
    private String email;
    private String fechaNacimiento;
    private String rut;
    private TipoUsuarioResponseDTO tipoUsuario;
    private String direccionEnvio;
}