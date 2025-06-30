package GestionUsuarios.GestionUsuarios.service;

import GestionUsuarios.GestionUsuarios.DTO.PedidoRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.PedidoResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.PedidoMapper;
import GestionUsuarios.GestionUsuarios.model.Cliente;
import GestionUsuarios.GestionUsuarios.model.Pedido;
import GestionUsuarios.GestionUsuarios.repository.ClienteRepository;
import GestionUsuarios.GestionUsuarios.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar la lógica de negocio de los pedidos.
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoMapper pedidoMapper;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.pedidoMapper = pedidoMapper;
    }

    /**
     * Obtiene una lista de todos los pedidos.
     * @return Una lista de {@link PedidoResponseDTO}.
     */
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un pedido por su ID.
     * @param id El ID del pedido.
     * @return El {@link PedidoResponseDTO} del pedido encontrado.
     * @throws ResourceNotFoundException si no se encuentra el pedido.
     */
    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
        return pedidoMapper.toResponseDTO(pedido);
    }

    /**
     * Crea un nuevo pedido asociado a un cliente.
     * @param requestDTO DTO con los datos del nuevo pedido.
     * @return El {@link PedidoResponseDTO} del pedido creado.
     * @throws ResourceNotFoundException si el cliente asociado no existe.
     */
    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO requestDTO) {
        Cliente cliente = clienteRepository.findById(requestDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + requestDTO.getClienteId()));

        Pedido pedido = pedidoMapper.toEntity(requestDTO);
        pedido.setCliente(cliente);
        pedido.setFechaPedido(LocalDateTime.now());

        Pedido nuevoPedido = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDTO(nuevoPedido);
    }

    /**
     * Actualiza un pedido existente.
     * @param id El ID del pedido a actualizar.
     * @param requestDTO DTO con los nuevos datos.
     * @return El {@link PedidoResponseDTO} del pedido actualizado.
     * @throws ResourceNotFoundException si el pedido o el cliente no se encuentran.
     */
    @Transactional
    public PedidoResponseDTO actualizarPedido(Long id, PedidoRequestDTO requestDTO) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));

        pedidoMapper.updateEntityFromDto(requestDTO, pedidoExistente);

        if (pedidoExistente.getCliente() != null &&
            requestDTO.getClienteId() != null && 
            !pedidoExistente.getCliente().getId().equals(requestDTO.getClienteId())) {
             Cliente nuevoCliente = clienteRepository.findById(requestDTO.getClienteId())
                 .orElseThrow(() -> new ResourceNotFoundException("Cliente (nuevo) no encontrado con id: " + requestDTO.getClienteId()));
             pedidoExistente.setCliente(nuevoCliente);
        }

        Pedido pedidoActualizado = pedidoRepository.save(pedidoExistente);
        return pedidoMapper.toResponseDTO(pedidoActualizado);
    }

    /**
     * Elimina un pedido por su ID.
     * @param id El ID del pedido a eliminar.
     * @throws ResourceNotFoundException si el pedido no se encuentra.
     */
    @Transactional
    public void eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido no encontrado con id: " + id);
        }
        pedidoRepository.deleteById(id);
    }
}