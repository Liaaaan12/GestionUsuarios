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

import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.EmpleadoVentasMapper;
import GestionUsuarios.GestionUsuarios.model.EmpleadoVentas;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.EmpleadoVentasRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class EmpleadoVentasServiceTest {

    @Mock
    private EmpleadoVentasRepository empleadoVentasRepository;
    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;
    @Mock
    private EmpleadoVentasMapper empleadoVentasMapper;
    @InjectMocks
    private EmpleadoVentasService empleadoVentasService;

    private EmpleadoVentas empleado;
    private EmpleadoVentasRequestDTO requestDTO;
    private EmpleadoVentasResponseDTO responseDTO;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario(2L, "VENTAS");
        requestDTO = new EmpleadoVentasRequestDTO("Empleado de Prueba", "empleado@test.com", "password", "1998-11-20", "33444555-6", 2L, "2023-01-15", 500000.0);

        empleado = new EmpleadoVentas();
        empleado.setId(1L);
        empleado.setNombre("Empleado Uno");
        empleado.setTipoUsuario(tipoUsuario);

        responseDTO = new EmpleadoVentasResponseDTO(1L, "Empleado Uno", "empleado1@test.com", "1990-01-01", "44555666-7", null, "2020-01-01", 600000.0);
    }

    // Pruebas para obtenerTodosLosEmpleadosVentas
    @Test
    void testObtenerTodosLosEmpleadosVentas_Exitoso() {
        when(empleadoVentasRepository.findAll()).thenReturn(Collections.singletonList(empleado));
        when(empleadoVentasMapper.toResponseDTO(any(EmpleadoVentas.class))).thenReturn(responseDTO);

        List<EmpleadoVentasResponseDTO> resultado = empleadoVentasService.obtenerTodosLosEmpleadosVentas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(empleadoVentasRepository).findAll();
    }

    // Pruebas para obtenerEmpleadoVentasPorId
    @Test
    void testObtenerEmpleadoVentasPorId_Exitoso() {
        when(empleadoVentasRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(empleadoVentasMapper.toResponseDTO(empleado)).thenReturn(responseDTO);

        EmpleadoVentasResponseDTO resultado = empleadoVentasService.obtenerEmpleadoVentasPorId(1L);

        assertNotNull(resultado);
        assertEquals(responseDTO.getNombre(), resultado.getNombre());
    }

    @Test
    void testObtenerEmpleadoVentasPorId_NoEncontrado() {
        when(empleadoVentasRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> empleadoVentasService.obtenerEmpleadoVentasPorId(99L));
    }

    // Pruebas para crearEmpleadoVentas
    @Test
    void testCrearEmpleadoVentas_Exitoso() {
        when(tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));
        when(empleadoVentasMapper.toEntity(any(EmpleadoVentasRequestDTO.class))).thenReturn(empleado);
        when(empleadoVentasRepository.save(any(EmpleadoVentas.class))).thenReturn(empleado);
        when(empleadoVentasMapper.toResponseDTO(any(EmpleadoVentas.class))).thenReturn(responseDTO);

        EmpleadoVentasResponseDTO resultado = empleadoVentasService.crearEmpleadoVentas(requestDTO);

        assertNotNull(resultado);
        assertEquals(responseDTO.getNombre(), resultado.getNombre());
        verify(empleadoVentasRepository).save(empleado);
    }
    
    @Test
    void testCrearEmpleadoVentas_TipoUsuarioNoEncontrado() {
        when(tipoUsuarioRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> empleadoVentasService.crearEmpleadoVentas(requestDTO));
    }

    // Pruebas para actualizarEmpleadoVentas
    @Test
    void testActualizarEmpleadoVentas_Exitoso() {
        when(empleadoVentasRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));
        when(empleadoVentasRepository.save(any(EmpleadoVentas.class))).thenReturn(empleado);

        requestDTO.setPassword("newPassword"); // Probando la actualización de contraseña
        empleadoVentasService.actualizarEmpleadoVentas(1L, requestDTO);

        verify(empleadoVentasMapper).updateEntityFromDto(requestDTO, empleado);
        verify(empleadoVentasRepository).save(empleado);
        assertEquals("newPassword", empleado.getPassword());
    }

    @Test
    void testActualizarEmpleadoVentas_EmpleadoNoEncontrado() {
        when(empleadoVentasRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> empleadoVentasService.actualizarEmpleadoVentas(99L, requestDTO));
    }

    @Test
    void testActualizarEmpleadoVentas_TipoUsuarioNoEncontrado() {
        when(empleadoVentasRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(tipoUsuarioRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> empleadoVentasService.actualizarEmpleadoVentas(1L, requestDTO));
    }

    // Pruebas para eliminarEmpleadoVentas
    @Test
    void testEliminarEmpleadoVentas_Exitoso() {
        when(empleadoVentasRepository.existsById(1L)).thenReturn(true);
        doNothing().when(empleadoVentasRepository).deleteById(1L);

        empleadoVentasService.eliminarEmpleadoVentas(1L);

        verify(empleadoVentasRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarEmpleadoVentas_NoEncontrado() {
        when(empleadoVentasRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> empleadoVentasService.eliminarEmpleadoVentas(99L));
    }
}