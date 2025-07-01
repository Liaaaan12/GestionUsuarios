package GestionUsuarios.GestionUsuarios.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.service.GerenteTiendaService;

@WebMvcTest(GerenteTiendaController.class)
public class GerenteTiendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GerenteTiendaService gerenteTiendaService;

    @Autowired
    private ObjectMapper objectMapper;

    private GerenteTiendaResponseDTO responseDTO;
    private GerenteTiendaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new GerenteTiendaResponseDTO(1L, "Gerente de Prueba", "gerente@test.com", "1985-03-15", "11222333-k", null, 5, "Tienda Central");
        requestDTO = new GerenteTiendaRequestDTO("Gerente de Prueba", "gerente@test.com", "password", "1985-03-15", "11222333-k", 3L, 5, "Tienda Central");
    }

    @Test
    void testObtenerTodosLosGerentes() throws Exception {
        when(gerenteTiendaService.obtenerTodosLosGerentes()).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/gerentes-tienda"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.gerenteTiendaResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.gerenteTiendaResponseDTOList[0].nombre", is("Gerente de Prueba")));
    }

    @Test
    void testObtenerGerentePorId_Exitoso() throws Exception {
        // Prueba para el camino feliz (200 OK)
        when(gerenteTiendaService.obtenerGerentePorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/gerentes-tienda/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Gerente de Prueba")))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testObtenerGerentePorId_NoEncontrado() throws Exception {
        // MODIFICACIÓN CLAVE: Prueba para el camino de error (404 Not Found)
        // Esto asegura que se cubra la excepción lanzada por el servicio.
        Long idNoExistente = 99L;
        when(gerenteTiendaService.obtenerGerentePorId(idNoExistente))
                .thenThrow(new ResourceNotFoundException("Gerente de Tienda no encontrado con id: " + idNoExistente));

        mockMvc.perform(get("/gerentes-tienda/{id}", idNoExistente))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearGerente() throws Exception {
        when(gerenteTiendaService.crearGerente(any(GerenteTiendaRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/gerentes-tienda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizarGerente() throws Exception {
        when(gerenteTiendaService.actualizarGerente(eq(1L), any(GerenteTiendaRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/gerentes-tienda/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarGerente() throws Exception {
        mockMvc.perform(delete("/gerentes-tienda/1"))
                .andExpect(status().isNoContent());
    }
}