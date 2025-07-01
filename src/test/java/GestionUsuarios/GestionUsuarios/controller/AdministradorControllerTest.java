package GestionUsuarios.GestionUsuarios.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import GestionUsuarios.GestionUsuarios.DTO.AdministradorRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.AdministradorResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.service.AdministradorService;

@WebMvcTest(AdministradorController.class)
public class AdministradorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministradorService administradorService;

    @Autowired
    private ObjectMapper objectMapper;

    private AdministradorResponseDTO responseDTO;
    private AdministradorRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Corregido: Usar el constructor con todos los campos del DTO
        responseDTO = new AdministradorResponseDTO(1L, "Admin de Prueba", "admin@test.com", "2000-01-01", "12345678-9", null);
        requestDTO = new AdministradorRequestDTO("Admin de Prueba", "admin@test.com", "password", "2000-01-01", "12345678-9", 1L);
    }

    @Test
    void testObtenerTodosLosAdministradores() throws Exception {
        List<AdministradorResponseDTO> administradores = Collections.singletonList(responseDTO);
        when(administradorService.obtenerTodosLosAdministradores()).thenReturn(administradores);

        mockMvc.perform(get("/administradores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.administradorResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.administradorResponseDTOList[0].nombre", is("Admin de Prueba")));
    }
    
    @Test
    void testObtenerAdministradorPorId() throws Exception {
        when(administradorService.obtenerAdministradorPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/administradores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Admin de Prueba")));
    }

    @Test
    void testObtenerAdministradorPorId_NoEncontrado() throws Exception {
        when(administradorService.obtenerAdministradorPorId(99L)).thenThrow(new ResourceNotFoundException("Administrador no encontrado"));

        mockMvc.perform(get("/administradores/99"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testCrearAdministrador() throws Exception {
        when(administradorService.crearAdministrador(any(AdministradorRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/administradores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Admin de Prueba")));
    }

    @Test
    void testActualizarAdministrador() throws Exception {
        when(administradorService.actualizarAdministrador(eq(1L), any(AdministradorRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/administradores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testEliminarAdministrador() throws Exception {
        doNothing().when(administradorService).eliminarAdministrador(1L);

        mockMvc.perform(delete("/administradores/1"))
                .andExpect(status().isNoContent());
    }
}