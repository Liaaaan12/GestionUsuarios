package GestionUsuarios.GestionUsuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import GestionUsuarios.GestionUsuarios.DTO.ClienteRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.ClienteResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.ClienteMapper;
import GestionUsuarios.GestionUsuarios.model.Cliente;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.ClienteRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    // --- DATOS DE PRUEBA ---
    private Cliente cliente1;
    private ClienteResponseDTO responseDTO1;
    private TipoUsuario tipoUsuario;
    private ClienteRequestDTO requestDTO;
    

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario(1L, "CLIENTE");
        requestDTO = new ClienteRequestDTO("Cliente de Prueba", "cliente@test.com", "password", "1995-05-10", "22333444-5", 1L, "Calle Falsa 123");

        cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNombre("Cliente Uno");
        cliente1.setEmail("cliente1@test.com");
        cliente1.setRut("11222333-4");
        cliente1.setTipoUsuario(tipoUsuario);
        cliente1.setDireccionEnvio("Avenida Siempre Viva 742");
        
        responseDTO1 = new ClienteResponseDTO(1L, "Cliente Uno", "cliente1@test.com");
    }

    @Test
    void testObtenerTodosLosClientes() {
        // Arrange
        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        List<Cliente> listaDeClientes = Arrays.asList(cliente1, cliente2);
        
        when(clienteRepository.findAll()).thenReturn(listaDeClientes);
        when(clienteMapper.toResponseDTO(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente cliente = invocation.getArgument(0);
            return new ClienteResponseDTO(cliente.getId(), cliente.getNombre(), cliente.getEmail());
        });
        
        // Act
        List<ClienteResponseDTO> resultado = clienteService.obtenerTodosLosClientes();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(clienteRepository).findAll();
    }

    @Test
    void testCrearCliente_DeberiaGuardarCliente() {
        // Arrange
        when(tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));

        Cliente clienteSinId = new Cliente();
        clienteSinId.setNombre(requestDTO.getNombre());
        clienteSinId.setEmail(requestDTO.getEmail());

        when(clienteMapper.toEntity(requestDTO)).thenReturn(clienteSinId);

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setId(1L);
        clienteGuardado.setNombre(requestDTO.getNombre());
        clienteGuardado.setEmail(requestDTO.getEmail());
        clienteGuardado.setTipoUsuario(tipoUsuario);

        when(clienteRepository.save(clienteSinId)).thenReturn(clienteGuardado);
        when(clienteMapper.toResponseDTO(clienteGuardado)).thenReturn(new ClienteResponseDTO(1L, "Cliente de Prueba", "cliente@test.com"));


        // Act
        ClienteResponseDTO resultado = clienteService.crearCliente(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Cliente de Prueba", resultado.getNombre());
        verify(clienteRepository).save(clienteSinId);
    }

    @Test
    void testObtenerClientePorId() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente1));
        when(clienteMapper.toResponseDTO(cliente1)).thenReturn(responseDTO1);

        // Act
        ClienteResponseDTO resultado = clienteService.obtenerClientePorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Cliente Uno", resultado.getNombre());
    }

    @Test
    void testActualizarCliente_DeberiaActualizarDatos() {
        // Arrange
        Long clienteId = 1L;
        ClienteRequestDTO requestActualizado = new ClienteRequestDTO("Cliente Actualizado", "cliente.actualizado@test.com", "newpass", "1996-03-04", "11222333-4", 1L, "Nueva Direccion 456");

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente1));
        when(tipoUsuarioRepository.findById(requestActualizado.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente1);
        when(clienteMapper.toResponseDTO(any(Cliente.class))).thenReturn(new ClienteResponseDTO());


        // Act
        clienteService.actualizarCliente(clienteId, requestActualizado);

        // Assert
        verify(clienteMapper).updateEntityFromDto(requestActualizado, cliente1);
        verify(clienteRepository).save(cliente1);
    }

    @Test
    void testEliminarCliente() {
        // Arrange
        Long clienteId = 2L;

        when(clienteRepository.existsById(clienteId)).thenReturn(true); 
        doNothing().when(clienteRepository).deleteById(clienteId);

        // Act
        clienteService.eliminarCliente(clienteId);

        // Assert
        verify(clienteRepository, times(1)).deleteById(clienteId);
    }

    @Test
    void testEliminarClienteNoEncontrado() {
        // Arrange
        Long clienteId = 99L;
        when(clienteRepository.existsById(clienteId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.eliminarCliente(clienteId);
        });
    }
}