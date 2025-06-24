package GestionUsuarios.GestionUsuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired; // Importar Autowired
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import GestionUsuarios.GestionUsuarios.DTO.AdministradorRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.AdministradorResponseDTO;
import GestionUsuarios.GestionUsuarios.mapper.AdministradorMapper;
import GestionUsuarios.GestionUsuarios.model.Administrador;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.AdministradorRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;

@SpringBootTest
public class AdministradorServiceTest {

    @MockBean
    private AdministradorRepository administradorRepository;

    @MockBean
    private TipoUsuarioRepository tipoUsuarioRepository;

    @MockBean
    private AdministradorMapper administradorMapper;

    // --- CAMBIO PRINCIPAL AQUÍ ---
    // Se reemplaza @InjectMocks por @Autowired.
    // Le pedimos a Spring que nos dé una instancia del servicio
    // y él se encargará de inyectarle los mocks de arriba.
    @Autowired
    private AdministradorService administradorService;

    private Administrador administrador;
    private TipoUsuario tipoUsuario;
    private AdministradorRequestDTO requestDTO;
    private AdministradorResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Esta línea ya no es necesaria porque Spring se encarga de todo.
        // MockitoAnnotations.openMocks(this);

        // Preparamos los datos de prueba
        tipoUsuario = new TipoUsuario(1L, "ADMIN");
        administrador = new Administrador();
        administrador.setId(1L);
        administrador.setNombre("Admin de Prueba");
        administrador.setEmail("admin@test.com");
        administrador.setRut("12345678-9");
        administrador.setTipoUsuario(tipoUsuario);

        requestDTO = new AdministradorRequestDTO("Admin de Prueba", "admin@test.com", "password", "2000-01-01", "12345678-9", 1L);
        
        responseDTO = new AdministradorResponseDTO(1L, "Admin de Prueba", "admin@test.com", "2000-01-01", "12345678-9", null);
    }

    @Test
    void testCrearAdministrador() {
        // Definimos el comportamiento de los mocks
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(administradorMapper.toEntity(any(AdministradorRequestDTO.class))).thenReturn(administrador);
        when(administradorRepository.save(any(Administrador.class))).thenReturn(administrador);
        when(administradorMapper.toResponseDTO(any(Administrador.class))).thenReturn(responseDTO);

        // Llamamos al método que queremos probar
        AdministradorResponseDTO resultado = administradorService.crearAdministrador(requestDTO);

        // Verificamos que el resultado es el esperado
        assertNotNull(resultado);
        assertEquals("Admin de Prueba", resultado.getNombre());
    }

    @Test
    void testObtenerAdministradorPorId() {
        // Definimos el comportamiento de los mocks
        when(administradorRepository.findById(1L)).thenReturn(Optional.of(administrador));
        when(administradorMapper.toResponseDTO(administrador)).thenReturn(responseDTO);

        // Llamamos al método
        AdministradorResponseDTO resultado = administradorService.obtenerAdministradorPorId(1L);

        // Verificamos
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }
}