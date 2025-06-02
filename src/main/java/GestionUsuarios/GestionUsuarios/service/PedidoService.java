package GestionUsuarios.GestionUsuarios.service;


import com.usuarios.dto.PedidoRequestDTO;
import com.usuarios.dto.PedidoResponseDTO;
import com.usuarios.exception.ResourceNotFoundException;
import com.usuarios.mapper.PedidoMapper;
import com.usuarios.model.Cliente;
import com.usuarios.model.Pedido;
import com.usuarios.repository.ClienteRepository;
import com.usuarios.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoMapper pedidoMapper;

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
        return pedidoMapper.toResponseDTO(pedido);
    }

    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO requestDTO) {
        Cliente cliente = clienteRepository.findById(requestDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + requestDTO.getClienteId()));

        Pedido pedido = pedidoMapper.toEntity(requestDTO);
        pedido.setCliente(cliente);
        pedido.setFechaPedido(LocalDateTime.now()); // Asegurar que la fecha se establece aquí

        Pedido nuevoPedido = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDTO(nuevoPedido);
    }

    @Transactional
    public PedidoResponseDTO actualizarPedido(Long id, PedidoRequestDTO requestDTO) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));

        // Opcional: permitir cambiar el cliente de un pedido (generalmente no se hace)
        // Si se permite, cargar el nuevo cliente:
        // Cliente cliente = clienteRepository.findById(requestDTO.getClienteId())
        //        .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + requestDTO.getClienteId()));
        // pedidoExistente.setCliente(cliente);

        pedidoMapper.updateEntityFromDto(requestDTO, pedidoExistente);

        // Si el clienteId en el DTO es diferente al actual, y se permite cambiarlo:
        if (!pedidoExistente.getCliente().getId().equals(requestDTO.getClienteId())) {
             Cliente nuevoCliente = clienteRepository.findById(requestDTO.getClienteId())
                 .orElseThrow(() -> new ResourceNotFoundException("Cliente (nuevo) no encontrado con id: " + requestDTO.getClienteId()));
             pedidoExistente.setCliente(nuevoCliente);
        }


        Pedido pedidoActualizado = pedidoRepository.save(pedidoExistente);
        return pedidoMapper.toResponseDTO(pedidoActualizado);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido no encontrado con id: " + id);
        }
        pedidoRepository.deleteById(id);
    }
}