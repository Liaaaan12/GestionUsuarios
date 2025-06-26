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

    // --- DATOS DE PRUEBA ---
    private Administrador administrador1;
    private AdministradorResponseDTO responseDTO1;
    private TipoUsuario tipoUsuario;
    private AdministradorRequestDTO requestDTO;
    

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario(1L, "ADMIN");
        requestDTO = new AdministradorRequestDTO("Admin de Prueba", "admin@test.com", "password", "2000-01-01", "12345678-9", 1L);

        administrador1 = new Administrador();
        administrador1.setId(1L);
        administrador1.setNombre("Admin Uno");
        administrador1.setEmail("admin1@test.com");
        administrador1.setRut("11111111-1");
        administrador1.setTipoUsuario(tipoUsuario);
        responseDTO1 = new AdministradorResponseDTO(1L, "Admin Uno", "admin1@test.com", "2000-01-01", "11111111-1", null);
    }

    @Test
    void testObtenerTodosLosAdministradores() {
        // Arrange
        Administrador administrador2 = new Administrador();
        administrador2.setId(2L);
        List<Administrador> listaDeAdmins = Arrays.asList(administrador1, administrador2);
        
        when(administradorRepository.findAll()).thenReturn(listaDeAdmins);
        when(administradorMapper.toResponseDTO(any(Administrador.class))).thenAnswer(invocation -> {
            Administrador admin = invocation.getArgument(0);
            return new AdministradorResponseDTO(admin.getId(), admin.getNombre(), admin.getEmail(), admin.getFechaNacimiento(), admin.getRut(), null);
        });
        
        // Act
        List<AdministradorResponseDTO> resultado = administradorService.obtenerTodosLosAdministradores();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(administradorRepository).findAll();
    }

    @Test
    void testCrearAdministrador_DeberiaGuardarAdministrador() {
        // Arrange
        when(tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));

        Administrador administradorSinId = new Administrador();
        administradorSinId.setNombre(requestDTO.getNombre());
        administradorSinId.setEmail(requestDTO.getEmail());

        when(administradorMapper.toEntity(requestDTO)).thenReturn(administradorSinId);

        Administrador administradorGuardado = new Administrador();
        administradorGuardado.setId(1L);
        administradorGuardado.setNombre(requestDTO.getNombre());
        administradorGuardado.setEmail(requestDTO.getEmail());
        administradorGuardado.setTipoUsuario(tipoUsuario);

        when(administradorRepository.save(administradorSinId)).thenReturn(administradorGuardado);
        when(administradorMapper.toResponseDTO(administradorGuardado)).thenReturn(new AdministradorResponseDTO(1L, "Admin de Prueba", "admin@test.com", "2000-01-01", "12345678-9", null));


        // Act
        AdministradorResponseDTO resultado = administradorService.crearAdministrador(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Admin de Prueba", resultado.getNombre());
        verify(administradorRepository).save(administradorSinId);
    }

    @Test
    void testObtenerAdministradorPorId() {
        // Arrange
        when(administradorRepository.findById(1L)).thenReturn(Optional.of(administrador1));
        when(administradorMapper.toResponseDTO(administrador1)).thenReturn(responseDTO1);

        // Act
        AdministradorResponseDTO resultado = administradorService.obtenerAdministradorPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Admin Uno", resultado.getNombre());
    }

    @Test
    void testActualizarAdministrador_DeberiaActualizarDatos() {
        // Arrange
        Long adminId = 1L;
        AdministradorRequestDTO requestActualizado = new AdministradorRequestDTO("Admin Actualizado", "admin.actualizado@test.com", "newpass", "2001-02-03", "11111111-1", 1L);

        when(administradorRepository.findById(adminId)).thenReturn(Optional.of(administrador1));
        when(tipoUsuarioRepository.findById(requestActualizado.getTipoUsuarioId())).thenReturn(Optional.of(tipoUsuario));
        when(administradorRepository.save(any(Administrador.class))).thenReturn(administrador1);
        when(administradorMapper.toResponseDTO(any(Administrador.class))).thenReturn(new AdministradorResponseDTO());


        // Act
        administradorService.actualizarAdministrador(adminId, requestActualizado);

        // Assert
        verify(administradorMapper).updateEntityFromDto(requestActualizado, administrador1);
        verify(administradorRepository).save(administrador1);
    }

    @Test
    void testEliminarAdministrador() {
        // Arrange
        Long adminId = 2L;

        when(administradorRepository.existsById(adminId)).thenReturn(true); 
        doNothing().when(administradorRepository).deleteById(adminId);

        // Act
        administradorService.eliminarAdministrador(adminId);

        // Assert
        verify(administradorRepository, times(1)).deleteById(adminId);
    }

    @Test
    void testEliminarAdministradorNoEncontrado() {
        // Arrange
        Long adminId = 99L;
        when(administradorRepository.existsById(adminId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            administradorService.eliminarAdministrador(adminId);
        });
    }
}