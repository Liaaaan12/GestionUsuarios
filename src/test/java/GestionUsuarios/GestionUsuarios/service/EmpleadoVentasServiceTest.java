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

    // --- DATOS DE PRUEBA ---
    private EmpleadoVentas empleado1;
    private EmpleadoVentasResponseDTO responseDTO1;
    private TipoUsuario tipoUsuario;
    private EmpleadoVentasRequestDTO requestDTO;
    

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario(2L, "VENTAS");
        requestDTO = new EmpleadoVentasRequestDTO("Empleado de Prueba", "empleado@test.com", "password", "1998-11-20", "33444555-6", 2L, "2023-01-15", 500000.0);

        empleado1 = new EmpleadoVentas();
        empleado1.setId(1L);
        empleado1.setNombre("Empleado Uno");
        empleado1.setEmail("empleado1@test.com");
        empleado1.setRut("44555666-7");
        empleado1.setTipoUsuario(tipoUsuario);
        
        responseDTO1 = new EmpleadoVentasResponseDTO(1L, "Empleado Uno", "empleado1@test.com", "1990-01-01", "44555666-7", null, "2020-01-01", 600000.0);
    }

    @Test
    void testObtenerTodosLosEmpleadosVentas() {
        // Arrange
        EmpleadoVentas empleado2 = new EmpleadoVentas();
        empleado2.setId(2L);
        List<EmpleadoVentas> listaDeEmpleados = Arrays.asList(empleado1, empleado2);
        
        when(empleadoVentasRepository.findAll()).thenReturn(listaDeEmpleados);
        when(empleadoVentasMapper.toResponseDTO(any(EmpleadoVentas.class))).thenAnswer(invocation -> {
            EmpleadoVentas empleado = invocation.getArgument(0);
            return new EmpleadoVentasResponseDTO(empleado.getId(), empleado.getNombre(), empleado.getEmail(), null, null, null, null, null);
        });
        
        // Act
        List<EmpleadoVentasResponseDTO> resultado = empleadoVentasService.obtenerTodosLosEmpleadosVentas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(empleadoVentasRepository).findAll();
    }

    @Test
    void testCrearEmpleadoVentas_DeberiaGuardarEmpleado() {
        // Arrange
        when(tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));

        EmpleadoVentas empleadoSinId = new EmpleadoVentas();
        empleadoSinId.setNombre(requestDTO.getNombre());
        
        when(empleadoVentasMapper.toEntity(requestDTO)).thenReturn(empleadoSinId);

        EmpleadoVentas empleadoGuardado = new EmpleadoVentas();
        empleadoGuardado.setId(1L);
        empleadoGuardado.setNombre(requestDTO.getNombre());
        empleadoGuardado.setTipoUsuario(tipoUsuario);

        when(empleadoVentasRepository.save(empleadoSinId)).thenReturn(empleadoGuardado);
        when(empleadoVentasMapper.toResponseDTO(empleadoGuardado)).thenReturn(new EmpleadoVentasResponseDTO(1L, "Empleado de Prueba", "empleado@test.com", null, null, null, null, null));


        // Act
        EmpleadoVentasResponseDTO resultado = empleadoVentasService.crearEmpleadoVentas(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Empleado de Prueba", resultado.getNombre());
        verify(empleadoVentasRepository).save(empleadoSinId);
    }

    @Test
    void testObtenerEmpleadoVentasPorId() {
        // Arrange
        when(empleadoVentasRepository.findById(1L)).thenReturn(Optional.of(empleado1));
        when(empleadoVentasMapper.toResponseDTO(empleado1)).thenReturn(responseDTO1);

        // Act
        EmpleadoVentasResponseDTO resultado = empleadoVentasService.obtenerEmpleadoVentasPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Empleado Uno", resultado.getNombre());
    }

    @Test
    void testActualizarEmpleadoVentas_DeberiaActualizarDatos() {
        // Arrange
        Long empleadoId = 1L;
        EmpleadoVentasRequestDTO requestActualizado = new EmpleadoVentasRequestDTO("Empleado Actualizado", "empleado.actualizado@test.com", "newpass", "1999-12-21", "33444555-6", 2L, "2024-02-16", 550000.0);

        when(empleadoVentasRepository.findById(empleadoId)).thenReturn(Optional.of(empleado1));
        when(tipoUsuarioRepository.findById(requestActualizado.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));
        when(empleadoVentasRepository.save(any(EmpleadoVentas.class))).thenReturn(empleado1);
        when(empleadoVentasMapper.toResponseDTO(any(EmpleadoVentas.class))).thenReturn(new EmpleadoVentasResponseDTO());


        // Act
        empleadoVentasService.actualizarEmpleadoVentas(empleadoId, requestActualizado);

        // Assert
        verify(empleadoVentasMapper).updateEntityFromDto(requestActualizado, empleado1);
        verify(empleadoVentasRepository).save(empleado1);
    }

    @Test
    void testEliminarEmpleadoVentas() {
        // Arrange
        Long empleadoId = 2L;

        when(empleadoVentasRepository.existsById(empleadoId)).thenReturn(true); 
        doNothing().when(empleadoVentasRepository).deleteById(empleadoId);

        // Act
        empleadoVentasService.eliminarEmpleadoVentas(empleadoId);

        // Assert
        verify(empleadoVentasRepository, times(1)).deleteById(empleadoId);
    }

    @Test
    void testEliminarEmpleadoVentasNoEncontrado() {
        // Arrange
        Long empleadoId = 99L;
        when(empleadoVentasRepository.existsById(empleadoId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            empleadoVentasService.eliminarEmpleadoVentas(empleadoId);
        });
    }
}