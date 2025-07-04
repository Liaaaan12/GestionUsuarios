package GestionUsuarios.GestionUsuarios.service;

import GestionUsuarios.GestionUsuarios.DTO.PedidoRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.PedidoResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.PedidoMapper;
import GestionUsuarios.GestionUsuarios.model.Cliente;
import GestionUsuarios.GestionUsuarios.model.Pedido;
import GestionUsuarios.GestionUsuarios.repository.ClienteRepository;
import GestionUsuarios.GestionUsuarios.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoMapper pedidoMapper;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.pedidoMapper = pedidoMapper;
    }

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
        pedido.setFechaPedido(LocalDateTime.now());

        Pedido nuevoPedido = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDTO(nuevoPedido);
    }

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

    @Transactional
    public void eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido no encontrado con id: " + id);
        }
        pedidoRepository.deleteById(id);
    }
}