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

    private GerenteTienda gerente;
    private GerenteTiendaRequestDTO requestDTO;
    private GerenteTiendaResponseDTO responseDTO;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario(3L, "GERENTE");
        requestDTO = new GerenteTiendaRequestDTO("Gerente de Prueba", "gerente@test.com", "password", "1985-03-15", "11222333-k", 3L, 5, "Tienda Central");

        gerente = new GerenteTienda();
        gerente.setId(1L);
        gerente.setNombre("Gerente Uno");
        gerente.setTipoUsuario(tipoUsuario);

        responseDTO = new GerenteTiendaResponseDTO(1L, "Gerente Uno", "gerente1@test.com", "1980-01-01", "12345678-9", null, 10, "Tienda Norte");
    }

    // Pruebas para obtenerTodosLosGerentes
    @Test
    void testObtenerTodosLosGerentes_Exitoso() {
        when(gerenteTiendaRepository.findAll()).thenReturn(Collections.singletonList(gerente));
        when(gerenteTiendaMapper.toResponseDTO(any(GerenteTienda.class))).thenReturn(responseDTO);

        List<GerenteTiendaResponseDTO> resultado = gerenteTiendaService.obtenerTodosLosGerentes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(gerenteTiendaRepository).findAll();
    }

    // Pruebas para obtenerGerentePorId
    @Test
    void testObtenerGerentePorId_Exitoso() {
        when(gerenteTiendaRepository.findById(1L)).thenReturn(Optional.of(gerente));
        when(gerenteTiendaMapper.toResponseDTO(gerente)).thenReturn(responseDTO);

        GerenteTiendaResponseDTO resultado = gerenteTiendaService.obtenerGerentePorId(1L);

        assertNotNull(resultado);
        assertEquals(responseDTO.getNombre(), resultado.getNombre());
    }

    @Test
    void testObtenerGerentePorId_NoEncontrado() {
        when(gerenteTiendaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gerenteTiendaService.obtenerGerentePorId(99L));
    }

    // Pruebas para crearGerente
    @Test
    void testCrearGerente_Exitoso() {
        when(tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));
        when(gerenteTiendaMapper.toEntity(any(GerenteTiendaRequestDTO.class))).thenReturn(gerente);
        when(gerenteTiendaRepository.save(any(GerenteTienda.class))).thenReturn(gerente);
        when(gerenteTiendaMapper.toResponseDTO(any(GerenteTienda.class))).thenReturn(responseDTO);

        GerenteTiendaResponseDTO resultado = gerenteTiendaService.crearGerente(requestDTO);

        assertNotNull(resultado);
        assertEquals(responseDTO.getNombre(), resultado.getNombre());
        verify(gerenteTiendaRepository).save(gerente);
    }
    
    @Test
    void testCrearGerente_TipoUsuarioNoEncontrado() {
        when(tipoUsuarioRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gerenteTiendaService.crearGerente(requestDTO));
    }

    // Pruebas para actualizarGerente
    @Test
    void testActualizarGerente_Exitoso() {
        when(gerenteTiendaRepository.findById(1L)).thenReturn(Optional.of(gerente));
        when(tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));
        when(gerenteTiendaRepository.save(any(GerenteTienda.class))).thenReturn(gerente);
        
        gerenteTiendaService.actualizarGerente(1L, requestDTO);

        verify(gerenteTiendaMapper).updateEntityFromDto(requestDTO, gerente);
        verify(gerenteTiendaRepository).save(gerente);
    }

    @Test
    void testActualizarGerente_GerenteNoEncontrado() {
        when(gerenteTiendaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gerenteTiendaService.actualizarGerente(99L, requestDTO));
    }
    
    @Test
    void testActualizarGerente_TipoUsuarioNoEncontrado() {
        when(gerenteTiendaRepository.findById(1L)).thenReturn(Optional.of(gerente));
        when(tipoUsuarioRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gerenteTiendaService.actualizarGerente(1L, requestDTO));
    }

    // Pruebas para eliminarGerente
    @Test
    void testEliminarGerente_Exitoso() {
        when(gerenteTiendaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(gerenteTiendaRepository).deleteById(1L);

        gerenteTiendaService.eliminarGerente(1L);

        verify(gerenteTiendaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarGerente_NoEncontrado() {
        when(gerenteTiendaRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> gerenteTiendaService.eliminarGerente(99L));
    }
}