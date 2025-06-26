package GestionUsuarios.GestionUsuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import GestionUsuarios.GestionUsuarios.DTO.ClienteResponseDTO;
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

    // --- DATOS DE PRUEBA ---
    private Pedido pedido1;
    private Cliente cliente;
    private PedidoResponseDTO responseDTO1;
    private PedidoRequestDTO requestDTO;
    

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Cliente de Prueba");

        requestDTO = new PedidoRequestDTO("Pendiente", 150.99, 1L, "Calle Falsa 123", "Tarjeta de Crédito");

        pedido1 = new Pedido();
        pedido1.setId(1L);
        pedido1.setEstado("Entregado");
        pedido1.setTotal(200.50);
        pedido1.setCliente(cliente);
        pedido1.setFechaPedido(LocalDateTime.now());
        
        ClienteResponseDTO clienteResponse = new ClienteResponseDTO(cliente.getId(), cliente.getNombre(), "cliente@test.com");
        responseDTO1 = new PedidoResponseDTO(1L, LocalDateTime.now(), "Entregado", 200.50, clienteResponse, "Calle Falsa 123", "Tarjeta");
    }

    @Test
    void testObtenerTodosLosPedidos() {
        // Arrange
        Pedido pedido2 = new Pedido();
        pedido2.setId(2L);
        List<Pedido> listaDePedidos = Arrays.asList(pedido1, pedido2);
        
        when(pedidoRepository.findAll()).thenReturn(listaDePedidos);
        when(pedidoMapper.toResponseDTO(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedido = invocation.getArgument(0);
            return new PedidoResponseDTO(pedido.getId(), pedido.getFechaPedido(), pedido.getEstado(), pedido.getTotal(), null, null, null);
        });
        
        // Act
        List<PedidoResponseDTO> resultado = pedidoService.obtenerTodosLosPedidos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pedidoRepository).findAll();
    }

    @Test
    void testCrearPedido_DeberiaGuardarPedido() {
        // Arrange
        when(clienteRepository.findById(requestDTO.getClienteId())).thenReturn(Optional.of(cliente));

        Pedido pedidoSinId = new Pedido();
        pedidoSinId.setEstado(requestDTO.getEstado());
        
        when(pedidoMapper.toEntity(requestDTO)).thenReturn(pedidoSinId);

        Pedido pedidoGuardado = new Pedido();
        pedidoGuardado.setId(1L);
        pedidoGuardado.setEstado(requestDTO.getEstado());
        pedidoGuardado.setCliente(cliente);

        when(pedidoRepository.save(pedidoSinId)).thenReturn(pedidoGuardado);
        when(pedidoMapper.toResponseDTO(pedidoGuardado)).thenReturn(new PedidoResponseDTO(1L, LocalDateTime.now(), "Pendiente", 150.99, null, null, null));


        // Act
        PedidoResponseDTO resultado = pedidoService.crearPedido(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pendiente", resultado.getEstado());
        verify(pedidoRepository).save(pedidoSinId);
    }

    @Test
    void testCrearPedido_CuandoClienteNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        PedidoRequestDTO requestConClienteInexistente = new PedidoRequestDTO("Pendiente", 100.0, 99L, "Direccion", "Efectivo");

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.crearPedido(requestConClienteInexistente);
        });
    }

    @Test
    void testObtenerPedidoPorId() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido1));
        when(pedidoMapper.toResponseDTO(pedido1)).thenReturn(responseDTO1);

        // Act
        PedidoResponseDTO resultado = pedidoService.obtenerPedidoPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Entregado", resultado.getEstado());
    }

    @Test
    void testActualizarPedido_DeberiaActualizarDatos() {
        // Arrange
        Long pedidoId = 1L;
        PedidoRequestDTO requestActualizado = new PedidoRequestDTO("Enviado", 155.00, 1L, "Nueva Direccion 456", "PayPal");

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido1));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido1);
        when(pedidoMapper.toResponseDTO(any(Pedido.class))).thenReturn(new PedidoResponseDTO());


        // Act
        pedidoService.actualizarPedido(pedidoId, requestActualizado);

        // Assert
        verify(pedidoMapper).updateEntityFromDto(requestActualizado, pedido1);
        verify(pedidoRepository).save(pedido1);
    }

    @Test
    void testEliminarPedido() {
        // Arrange
        Long pedidoId = 2L;

        when(pedidoRepository.existsById(pedidoId)).thenReturn(true); 
        doNothing().when(pedidoRepository).deleteById(pedidoId);

        // Act
        pedidoService.eliminarPedido(pedidoId);

        // Assert
        verify(pedidoRepository, times(1)).deleteById(pedidoId);
    }

    @Test
    void testEliminarPedidoNoEncontrado() {
        // Arrange
        Long pedidoId = 99L;
        when(pedidoRepository.existsById(pedidoId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.eliminarPedido(pedidoId);
        });
    }
}