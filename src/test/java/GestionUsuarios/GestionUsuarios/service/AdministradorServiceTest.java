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
    private Administrador administrador2;
    private AdministradorResponseDTO responseDTO2;
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

        administrador2 = new Administrador();
        administrador2.setId(2L);
        administrador2.setNombre("Admin Dos");
        administrador2.setEmail("admin2@test.com");
        administrador2.setRut("22222222-2");
        administrador2.setTipoUsuario(tipoUsuario);
        responseDTO2 = new AdministradorResponseDTO(2L, "Admin Dos", "admin2@test.com", "2000-01-01", "22222222-2", null);
    }

    @Test
    void testObtenerTodosLosAdministradores() {
        List<Administrador> listaDeAdmins = Arrays.asList(administrador1, administrador2);
        
        when(administradorRepository.findAll()).thenReturn(listaDeAdmins);
        when(administradorMapper.toResponseDTO(administrador1)).thenReturn(responseDTO1);
        when(administradorMapper.toResponseDTO(administrador2)).thenReturn(responseDTO2);
        
        List<AdministradorResponseDTO> resultado = administradorService.obtenerTodosLosAdministradores();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Admin Uno", resultado.get(0).getNombre());
        assertEquals("Admin Dos", resultado.get(1).getNombre());
    }

    @Test
    void testCrearAdministrador() {
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(administradorMapper.toEntity(any(AdministradorRequestDTO.class))).thenReturn(administrador1);
        when(administradorRepository.save(any(Administrador.class))).thenReturn(administrador1);
        when(administradorMapper.toResponseDTO(any(Administrador.class))).thenReturn(responseDTO1);

        AdministradorResponseDTO resultado = administradorService.crearAdministrador(requestDTO);

        assertNotNull(resultado);
        assertEquals("Admin Uno", resultado.getNombre());
    }

    @Test
    void testObtenerAdministradorPorId() {
        when(administradorRepository.findById(1L)).thenReturn(Optional.of(administrador1));
        when(administradorMapper.toResponseDTO(administrador1)).thenReturn(responseDTO1);

        AdministradorResponseDTO resultado = administradorService.obtenerAdministradorPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Admin Uno", resultado.getNombre());
    }

    // --- PRUEBA PARA ACTUALIZAR UN ADMINISTRADOR ---
    @Test
    void testActualizarAdministrador() {
        Long adminId = 1L;
        AdministradorRequestDTO requestActualizado = new AdministradorRequestDTO("Admin Uno Actualizado", "admin1.new@test.com", "newpass", "2001-02-03", "11111111-1", 1L);
        
        Administrador adminActualizado = new Administrador();
        adminActualizado.setId(adminId);
        adminActualizado.setNombre("Admin Uno Actualizado");

        AdministradorResponseDTO responseActualizado = new AdministradorResponseDTO(adminId, "Admin Uno Actualizado", "admin1.new@test.com", "2001-02-03", "11111111-1", null);

        when(administradorRepository.findById(adminId)).thenReturn(Optional.of(administrador1));
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(administradorRepository.save(any(Administrador.class))).thenReturn(adminActualizado);
        when(administradorMapper.toResponseDTO(any(Administrador.class))).thenReturn(responseActualizado);
        
        AdministradorResponseDTO resultado = administradorService.actualizarAdministrador(adminId, requestActualizado);
        
        assertNotNull(resultado);
        assertEquals("Admin Uno Actualizado", resultado.getNombre());
        verify(administradorMapper, times(1)).updateEntityFromDto(requestActualizado, administrador1);
    }

    // --- PRUEBA PARA ELIMINAR UN ADMINISTRADOR ---
    @Test
    void testEliminarAdministrador() {
        Long adminId = 2L;

        when(administradorRepository.existsById(adminId)).thenReturn(true); 
        doNothing().when(administradorRepository).deleteById(adminId);

        administradorService.eliminarAdministrador(adminId);

        verify(administradorRepository, times(1)).deleteById(adminId);
    }

    @Test
    void testEliminarAdministradorNoEncontrado() {
        Long adminId = 99L;
        when(administradorRepository.existsById(adminId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            administradorService.eliminarAdministrador(adminId);
        });
    }
}