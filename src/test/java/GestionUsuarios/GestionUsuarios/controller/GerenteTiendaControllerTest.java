package GestionUsuarios.GestionUsuarios.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

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
        when(gerenteTiendaService.obtenerTodosLosGerentes()).thenReturn(Arrays.asList(responseDTO));

        mockMvc.perform(get("/gerentes-tienda"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Gerente de Prueba"));
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