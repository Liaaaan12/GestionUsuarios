package GestionUsuarios.GestionUsuarios.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime fechaPedido;
    private String estado;
    private Double total;
    private ClienteResponseDTO cliente; // Anidar DTO del Cliente
    private String direccionEnvio;
    private String metodoPago;
}