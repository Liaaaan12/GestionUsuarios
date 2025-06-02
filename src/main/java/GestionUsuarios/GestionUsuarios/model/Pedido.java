package GestionUsuarios.GestionUsuarios.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDateTime fechaPedido; //

    @Column(nullable = false)
    private String estado; //

    @Column(nullable = false)
    private Double total; //

    @ManyToOne(fetch = FetchType.LAZY) // EAGER si siempre necesitas el cliente con el pedido
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "direccion_envio", nullable = false)
    private String direccionEnvio; //

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago; //
}