package GestionUsuarios.GestionUsuarios.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import GestionUsuarios.GestionUsuarios.DTO.PedidoRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.PedidoResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.service.PedidoService;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    private PedidoResponseDTO responseDTO;
    private PedidoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        responseDTO = new PedidoResponseDTO(1L, LocalDateTime.now(), "Pendiente", 150.99, null, "Calle Falsa 123", "Tarjeta de Crédito");
        requestDTO = new PedidoRequestDTO("Pendiente", 150.99, 1L, "Calle Falsa 123", "Tarjeta de Crédito");
    }

    @Test
    void testObtenerTodosLosPedidos() throws Exception {
        when(pedidoService.obtenerTodosLosPedidos()).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pedidoResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.pedidoResponseDTOList[0].estado", is("Pendiente")));
    }

    @Test
    void testObtenerPedidoPorId_Exitoso() throws Exception {
        // Prueba para el camino feliz (200 OK)
        when(pedidoService.obtenerPedidoPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.estado", is("Pendiente")))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testObtenerPedidoPorId_NoEncontrado() throws Exception {
        // MODIFICACIÓN CLAVE: Prueba para el camino de error (404 Not Found)
        // Esto asegura que se cubra la excepción lanzada por el servicio.
        Long idNoExistente = 99L;
        when(pedidoService.obtenerPedidoPorId(idNoExistente))
                .thenThrow(new ResourceNotFoundException("Pedido no encontrado con id: " + idNoExistente));

        mockMvc.perform(get("/pedidos/{id}", idNoExistente))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearPedido() throws Exception {
        when(pedidoService.crearPedido(any(PedidoRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizarPedido() throws Exception {
        when(pedidoService.actualizarPedido(eq(1L), any(PedidoRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarPedido() throws Exception {
        mockMvc.perform(delete("/pedidos/1"))
                .andExpect(status().isNoContent());
    }
}