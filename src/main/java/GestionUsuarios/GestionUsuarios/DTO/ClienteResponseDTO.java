package GestionUsuarios.GestionUsuarios.DTO;

// Importar PedidoResponseDTO si se va a anidar la lista de pedidos
// import java.util.List; 
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private String fechaNacimiento;
    private String rut;
    private TipoUsuarioResponseDTO tipoUsuario;
    private String direccionEnvio;
    // private List<PedidoResponseDTO> pedidos; // Opcional: incluir pedidos
}