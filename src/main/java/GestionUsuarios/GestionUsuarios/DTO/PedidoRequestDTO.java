package GestionUsuarios.GestionUsuarios.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {

    // La fecha del pedido usualmente se establece en el servidor al crear.
    // Si el cliente puede enviarla, se debe validar.
    // private LocalDateTime fechaPedido;

    @NotBlank(message = "El estado del pedido no puede estar vacío")
    private String estado;

    @NotNull(message = "El total del pedido no puede ser nulo")
    @Positive(message = "El total del pedido debe ser positivo")
    private Double total;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @NotBlank(message = "La dirección de envío no puede estar vacía")
    private String direccionEnvio;

    @NotBlank(message = "El método de pago no puede estar vacío")
    private String metodoPago;
}
