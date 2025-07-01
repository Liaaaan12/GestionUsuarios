package GestionUsuarios.GestionUsuarios.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PedidoResponseDTO extends RepresentationModel<PedidoResponseDTO> {
    private Long id;
    private LocalDateTime fechaPedido;
    private String estado;
    private Double total;
    private ClienteResponseDTO cliente;
    private String direccionEnvio;
    private String metodoPago;
}