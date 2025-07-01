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

import GestionUsuarios.GestionUsuarios.DTO.AdministradorRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.AdministradorResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.AdministradorMapper;
import GestionUsuarios.GestionUsuarios.model.Administrador;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.AdministradorRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class AdministradorServiceTest {

    @Mock
    private AdministradorRepository administradorRepository;
    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;
    @Mock
    private AdministradorMapper administradorMapper;
    @InjectMocks
    private AdministradorService administradorService;

    private Administrador administrador;
    private AdministradorRequestDTO requestDTO;
    private AdministradorResponseDTO responseDTO;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario(1L, "ADMIN");
        requestDTO = new AdministradorRequestDTO("Admin de Prueba", "admin@test.com", "password", "2000-01-01", "12345678-9", 1L);

        administrador = new Administrador();
        administrador.setId(1L);
        administrador.setNombre("Admin Uno");
        administrador.setEmail("admin1@test.com");
        administrador.setTipoUsuario(tipoUsuario);
        
        responseDTO = new AdministradorResponseDTO(1L, "Admin Uno", "admin1@test.com", "2000-01-01", "11111111-1", null);
    }
    
    // Pruebas para obtenerTodosLosAdministradores
    @Test
    void testObtenerTodosLosAdministradores_Exitoso() {
        when(administradorRepository.findAll()).thenReturn(Collections.singletonList(administrador));
        when(administradorMapper.toResponseDTO(any(Administrador.class))).thenReturn(responseDTO);

        List<AdministradorResponseDTO> resultado = administradorService.obtenerTodosLosAdministradores();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(administradorRepository).findAll();
    }

    // Pruebas para obtenerAdministradorPorId
    @Test
    void testObtenerAdministradorPorId_Exitoso() {
        when(administradorRepository.findById(1L)).thenReturn(Optional.of(administrador));
        when(administradorMapper.toResponseDTO(administrador)).thenReturn(responseDTO);

        AdministradorResponseDTO resultado = administradorService.obtenerAdministradorPorId(1L);

        assertNotNull(resultado);
        assertEquals(responseDTO.getNombre(), resultado.getNombre());
    }

    @Test
    void testObtenerAdministradorPorId_NoEncontrado() {
        when(administradorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> administradorService.obtenerAdministradorPorId(99L));
    }

    // Pruebas para crearAdministrador
    @Test
    void testCrearAdministrador_Exitoso() {
        when(tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));
        when(administradorMapper.toEntity(any(AdministradorRequestDTO.class))).thenReturn(administrador);
        when(administradorRepository.save(any(Administrador.class))).thenReturn(administrador);
        when(administradorMapper.toResponseDTO(any(Administrador.class))).thenReturn(responseDTO);

        AdministradorResponseDTO resultado = administradorService.crearAdministrador(requestDTO);

        assertNotNull(resultado);
        assertEquals(responseDTO.getNombre(), resultado.getNombre());
        verify(administradorRepository).save(administrador);
    }
    
    @Test
    void testCrearAdministrador_TipoUsuarioNoEncontrado() {
        when(tipoUsuarioRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> administradorService.crearAdministrador(requestDTO));
    }

    // Pruebas para actualizarAdministrador
    @Test
    void testActualizarAdministrador_Exitoso() {
        when(administradorRepository.findById(1L)).thenReturn(Optional.of(administrador));
        when(tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));
        when(administradorRepository.save(any(Administrador.class))).thenReturn(administrador);
        when(administradorMapper.toResponseDTO(any(Administrador.class))).thenReturn(responseDTO);
        
        administradorService.actualizarAdministrador(1L, requestDTO);

        verify(administradorMapper).updateEntityFromDto(requestDTO, administrador);
        verify(administradorRepository).save(administrador);
    }

    @Test
    void testActualizarAdministrador_AdministradorNoEncontrado() {
        when(administradorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> administradorService.actualizarAdministrador(99L, requestDTO));
    }
    
    @Test
    void testActualizarAdministrador_TipoUsuarioNoEncontrado() {
        when(administradorRepository.findById(1L)).thenReturn(Optional.of(administrador));
        when(tipoUsuarioRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> administradorService.actualizarAdministrador(1L, requestDTO));
    }

    // Pruebas para eliminarAdministrador
    @Test
    void testEliminarAdministrador_Exitoso() {
        when(administradorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(administradorRepository).deleteById(1L);

        administradorService.eliminarAdministrador(1L);

        verify(administradorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarAdministrador_NoEncontrado() {
        when(administradorRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> administradorService.eliminarAdministrador(99L));
    }
}