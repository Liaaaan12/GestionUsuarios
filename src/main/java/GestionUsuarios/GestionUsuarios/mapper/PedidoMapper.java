package GestionUsuarios.GestionUsuarios.mapper;

// Corrected imports
import GestionUsuarios.GestionUsuarios.DTO.PedidoRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.PedidoResponseDTO;
import GestionUsuarios.GestionUsuarios.model.Pedido;
// import GestionUsuarios.GestionUsuarios.model.Cliente; // Referenced by Pedido model

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class PedidoMapper {

    @Autowired
    private ClienteMapper clienteMapper; // Necesario para mapear el cliente anidado

    public PedidoResponseDTO toResponseDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setEstado(pedido.getEstado());
        dto.setTotal(pedido.getTotal());
        dto.setCliente(clienteMapper.toResponseDTO(pedido.getCliente())); // Mapear Cliente a ClienteResponseDTO
        dto.setDireccionEnvio(pedido.getDireccionEnvio());
        dto.setMetodoPago(pedido.getMetodoPago());
        return dto;
    }

    public Pedido toEntity(PedidoRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        Pedido pedido = new Pedido();
        // La fecha del pedido usualmente se establece en el servicio al crear
        pedido.setFechaPedido(LocalDateTime.now()); // O tomarla del DTO si se permite
        pedido.setEstado(requestDTO.getEstado());
        pedido.setTotal(requestDTO.getTotal());
        // El cliente se asociará en el servicio
        pedido.setDireccionEnvio(requestDTO.getDireccionEnvio());
        pedido.setMetodoPago(requestDTO.getMetodoPago());
        return pedido;
    }

    public void updateEntityFromDto(PedidoRequestDTO requestDTO, Pedido pedido) {
        if (requestDTO == null || pedido == null) {
            return;
        }
        // La fecha del pedido raramente se actualiza, pero si es necesario:
        // if (requestDTO.getFechaPedido() != null) {
        //     pedido.setFechaPedido(requestDTO.getFechaPedido());
        // }
        pedido.setEstado(requestDTO.getEstado());
        pedido.setTotal(requestDTO.getTotal());
        pedido.setDireccionEnvio(requestDTO.getDireccionEnvio());
        pedido.setMetodoPago(requestDTO.getMetodoPago());
        // La actualización del cliente asociado se manejaría en el servicio si es necesario
    }
}