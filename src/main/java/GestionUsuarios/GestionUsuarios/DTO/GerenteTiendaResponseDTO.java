package GestionUsuarios.GestionUsuarios.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GerenteTiendaResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private String fechaNacimiento;
    private String rut;
    private TipoUsuarioResponseDTO tipoUsuario;
    private Integer anosExperiencia;
    private String tiendaAsignada;
}