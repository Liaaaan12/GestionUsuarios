package GestionUsuarios.GestionUsuarios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import GestionUsuarios.GestionUsuarios.DTO.PedidoRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.PedidoResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.PedidoMapper;
import GestionUsuarios.GestionUsuarios.model.Cliente;
import GestionUsuarios.GestionUsuarios.model.Pedido;
import GestionUsuarios.GestionUsuarios.repository.ClienteRepository;
import GestionUsuarios.GestionUsuarios.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private PedidoMapper pedidoMapper;
    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private Cliente cliente;
    private PedidoRequestDTO requestDTO;
    private PedidoResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Cliente de Prueba");

        requestDTO = new PedidoRequestDTO("Pendiente", 150.99, 1L, "Calle Falsa 123", "Tarjeta de Crédito");

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado("Entregado");
        pedido.setCliente(cliente);

        responseDTO = new PedidoResponseDTO(1L, LocalDateTime.now(), "Entregado", 200.50, null, "Calle Falsa 123", "Tarjeta");
    }

    // Pruebas para obtenerTodosLosPedidos
    @Test
    void testObtenerTodosLosPedidos_Exitoso() {
        when(pedidoRepository.findAll()).thenReturn(Collections.singletonList(pedido));
        when(pedidoMapper.toResponseDTO(any(Pedido.class))).thenReturn(responseDTO);

        List<PedidoResponseDTO> resultado = pedidoService.obtenerTodosLosPedidos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pedidoRepository).findAll();
    }

    // Pruebas para obtenerPedidoPorId
    @Test
    void testObtenerPedidoPorId_Exitoso() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoMapper.toResponseDTO(pedido)).thenReturn(responseDTO);

        PedidoResponseDTO resultado = pedidoService.obtenerPedidoPorId(1L);

        assertNotNull(resultado);
        assertEquals(responseDTO.getEstado(), resultado.getEstado());
    }

    @Test
    void testObtenerPedidoPorId_NoEncontrado() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.obtenerPedidoPorId(99L));
    }

    // Pruebas para crearPedido
    @Test
    void testCrearPedido_Exitoso() {
        when(clienteRepository.findById(requestDTO.getClienteId())).thenReturn(Optional.of(cliente));
        when(pedidoMapper.toEntity(any(PedidoRequestDTO.class))).thenReturn(pedido);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoMapper.toResponseDTO(any(Pedido.class))).thenReturn(responseDTO);

        PedidoResponseDTO resultado = pedidoService.crearPedido(requestDTO);

        assertNotNull(resultado);
        assertEquals(responseDTO.getEstado(), resultado.getEstado());
        verify(pedidoRepository).save(pedido);
    }
    
    @Test
    void testCrearPedido_ClienteNoEncontrado() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.crearPedido(requestDTO));
    }

    // Pruebas para actualizarPedido
    @Test
    void testActualizarPedido_Exitoso() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        
        pedidoService.actualizarPedido(1L, requestDTO);

        verify(pedidoMapper).updateEntityFromDto(requestDTO, pedido);
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void testActualizarPedido_CambiandoCliente_Exitoso() {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setId(2L);
        requestDTO.setClienteId(2L); // Cambiar el ID del cliente en el DTO

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(clienteRepository.findById(2L)).thenReturn(Optional.of(nuevoCliente));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        
        pedidoService.actualizarPedido(1L, requestDTO);

        verify(pedidoRepository).save(pedido);
        assertEquals(2L, pedido.getCliente().getId());
    }

    @Test
    void testActualizarPedido_PedidoNoEncontrado() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.actualizarPedido(99L, requestDTO));
    }
    
    @Test
    void testActualizarPedido_NuevoClienteNoEncontrado() {
        requestDTO.setClienteId(99L); // ID de un cliente que no existe
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.actualizarPedido(1L, requestDTO));
    }

    // Pruebas para eliminarPedido
    @Test
    void testEliminarPedido_Exitoso() {
        when(pedidoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pedidoRepository).deleteById(1L);

        pedidoService.eliminarPedido(1L);

        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarPedido_NoEncontrado() {
        when(pedidoRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.eliminarPedido(99L));
    }
}