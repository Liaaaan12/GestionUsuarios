package GestionUsuarios.GestionUsuarios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
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

    private Cliente cliente;
    private ClienteRequestDTO requestDTO;
    private ClienteResponseDTO responseDTO;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario(1L, "CLIENTE");
        requestDTO = new ClienteRequestDTO("Cliente de Prueba", "cliente@test.com", "password", "1995-05-10", "22333444-5", 1L, "Calle Falsa 123");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Cliente Uno");
        cliente.setTipoUsuario(tipoUsuario);
        
        responseDTO = new ClienteResponseDTO(1L, "Cliente Uno", "cliente1@test.com", "1995-05-10", "22333444-5", null, "Calle Falsa 123");
    }

    // Pruebas para obtenerTodosLosClientes
    @Test
    void testObtenerTodosLosClientes_Exitoso() {
        when(clienteRepository.findAll()).thenReturn(Collections.singletonList(cliente));
        when(clienteMapper.toResponseDTO(any(Cliente.class))).thenReturn(responseDTO);

        List<ClienteResponseDTO> resultado = clienteService.obtenerTodosLosClientes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(clienteRepository).findAll();
    }

    // Pruebas para obtenerClientePorId
    @Test
    void testObtenerClientePorId_Exitoso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponseDTO(cliente)).thenReturn(responseDTO);

        ClienteResponseDTO resultado = clienteService.obtenerClientePorId(1L);

        assertNotNull(resultado);
        assertEquals(responseDTO.getNombre(), resultado.getNombre());
    }

    @Test
    void testObtenerClientePorId_NoEncontrado() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clienteService.obtenerClientePorId(99L));
    }

    // Pruebas para crearCliente
    @Test
    void testCrearCliente_Exitoso() {
        when(tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));
        when(clienteMapper.toEntity(any(ClienteRequestDTO.class))).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toResponseDTO(any(Cliente.class))).thenReturn(responseDTO);

        ClienteResponseDTO resultado = clienteService.crearCliente(requestDTO);

        assertNotNull(resultado);
        assertEquals(responseDTO.getNombre(), resultado.getNombre());
        verify(clienteRepository).save(cliente);
    }
    
    @Test
    void testCrearCliente_TipoUsuarioNoEncontrado() {
        when(tipoUsuarioRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clienteService.crearCliente(requestDTO));
    }

    // Pruebas para actualizarCliente
    @Test
    void testActualizarCliente_Exitoso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        
        requestDTO.setPassword("newPassword"); // Probando la actualización de contraseña
        clienteService.actualizarCliente(1L, requestDTO);

        verify(clienteMapper).updateEntityFromDto(requestDTO, cliente);
        verify(clienteRepository).save(cliente);
        assertEquals("newPassword", cliente.getPassword());
    }

    @Test
    void testActualizarCliente_ClienteNoEncontrado() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clienteService.actualizarCliente(99L, requestDTO));
    }
    
    @Test
    void testActualizarCliente_TipoUsuarioNoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(tipoUsuarioRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clienteService.actualizarCliente(1L, requestDTO));
    }

    // Pruebas para eliminarCliente
    @Test
    void testEliminarCliente_Exitoso() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.eliminarCliente(1L);

        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarCliente_NoEncontrado() {
        when(clienteRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> clienteService.eliminarCliente(99L));
    }
}