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

import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.GerenteTiendaMapper;
import GestionUsuarios.GestionUsuarios.model.GerenteTienda;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.GerenteTiendaRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class GerenteTiendaServiceTest {

    @Mock
    private GerenteTiendaRepository gerenteTiendaRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private GerenteTiendaMapper gerenteTiendaMapper;

    @InjectMocks
    private GerenteTiendaService gerenteTiendaService;

    // --- DATOS DE PRUEBA ---
    private GerenteTienda gerente1;
    private GerenteTiendaResponseDTO responseDTO1;
    private TipoUsuario tipoUsuario;
    private GerenteTiendaRequestDTO requestDTO;
    

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario(3L, "GERENTE");
        requestDTO = new GerenteTiendaRequestDTO("Gerente de Prueba", "gerente@test.com", "password", "1985-03-15", "11222333-k", 3L, 5, "Tienda Central");

        gerente1 = new GerenteTienda();
        gerente1.setId(1L);
        gerente1.setNombre("Gerente Uno");
        gerente1.setEmail("gerente1@test.com");
        gerente1.setRut("12345678-9");
        gerente1.setTipoUsuario(tipoUsuario);
        
        responseDTO1 = new GerenteTiendaResponseDTO(1L, "Gerente Uno", "gerente1@test.com", "1980-01-01", "12345678-9", null, 10, "Tienda Norte");
    }

    @Test
    void testObtenerTodosLosGerentes() {
        // Arrange
        GerenteTienda gerente2 = new GerenteTienda();
        gerente2.setId(2L);
        List<GerenteTienda> listaDeGerentes = Arrays.asList(gerente1, gerente2);
        
        when(gerenteTiendaRepository.findAll()).thenReturn(listaDeGerentes);
        when(gerenteTiendaMapper.toResponseDTO(any(GerenteTienda.class))).thenAnswer(invocation -> {
            GerenteTienda gerente = invocation.getArgument(0);
            return new GerenteTiendaResponseDTO(gerente.getId(), gerente.getNombre(), gerente.getEmail(), null, null, null, null, null);
        });
        
        // Act
        List<GerenteTiendaResponseDTO> resultado = gerenteTiendaService.obtenerTodosLosGerentes();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(gerenteTiendaRepository).findAll();
    }

    @Test
    void testCrearGerente_DeberiaGuardarGerente() {
        // Arrange
        when(tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));

        GerenteTienda gerenteSinId = new GerenteTienda();
        gerenteSinId.setNombre(requestDTO.getNombre());
        
        when(gerenteTiendaMapper.toEntity(requestDTO)).thenReturn(gerenteSinId);

        GerenteTienda gerenteGuardado = new GerenteTienda();
        gerenteGuardado.setId(1L);
        gerenteGuardado.setNombre(requestDTO.getNombre());
        gerenteGuardado.setTipoUsuario(tipoUsuario);

        when(gerenteTiendaRepository.save(gerenteSinId)).thenReturn(gerenteGuardado);
        when(gerenteTiendaMapper.toResponseDTO(gerenteGuardado)).thenReturn(new GerenteTiendaResponseDTO(1L, "Gerente de Prueba", "gerente@test.com", null, null, null, null, null));


        // Act
        GerenteTiendaResponseDTO resultado = gerenteTiendaService.crearGerente(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Gerente de Prueba", resultado.getNombre());
        verify(gerenteTiendaRepository).save(gerenteSinId);
    }

    @Test
    void testObtenerGerentePorId() {
        // Arrange
        when(gerenteTiendaRepository.findById(1L)).thenReturn(Optional.of(gerente1));
        when(gerenteTiendaMapper.toResponseDTO(gerente1)).thenReturn(responseDTO1);

        // Act
        GerenteTiendaResponseDTO resultado = gerenteTiendaService.obtenerGerentePorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Gerente Uno", resultado.getNombre());
    }

    @Test
    void testActualizarGerente_DeberiaActualizarDatos() {
        // Arrange
        Long gerenteId = 1L;
        GerenteTiendaRequestDTO requestActualizado = new GerenteTiendaRequestDTO("Gerente Actualizado", "gerente.actualizado@test.com", "newpass", "1986-04-16", "11222333-k", 3L, 6, "Tienda Sur");

        when(gerenteTiendaRepository.findById(gerenteId)).thenReturn(Optional.of(gerente1));
        when(tipoUsuarioRepository.findById(requestActualizado.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));
        when(gerenteTiendaRepository.save(any(GerenteTienda.class))).thenReturn(gerente1);
        when(gerenteTiendaMapper.toResponseDTO(any(GerenteTienda.class))).thenReturn(new GerenteTiendaResponseDTO());


        // Act
        gerenteTiendaService.actualizarGerente(gerenteId, requestActualizado);

        // Assert
        verify(gerenteTiendaMapper).updateEntityFromDto(requestActualizado, gerente1);
        verify(gerenteTiendaRepository).save(gerente1);
    }

    @Test
    void testEliminarGerente() {
        // Arrange
        Long gerenteId = 2L;

        when(gerenteTiendaRepository.existsById(gerenteId)).thenReturn(true); 
        doNothing().when(gerenteTiendaRepository).deleteById(gerenteId);

        // Act
        gerenteTiendaService.eliminarGerente(gerenteId);

        // Assert
        verify(gerenteTiendaRepository, times(1)).deleteById(gerenteId);
    }

    @Test
    void testEliminarGerenteNoEncontrado() {
        // Arrange
        Long gerenteId = 99L;
        when(gerenteTiendaRepository.existsById(gerenteId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            gerenteTiendaService.eliminarGerente(gerenteId);
        });
    }
}