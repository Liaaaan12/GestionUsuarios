package GestionUsuarios.GestionUsuarios.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;

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
        objectMapper.registerModule(new JavaTimeModule()); // Para manejar LocalDateTime en JSON
        responseDTO = new PedidoResponseDTO(1L, LocalDateTime.now(), "Pendiente", 150.99, null, "Calle Falsa 123", "Tarjeta de Crédito");
        requestDTO = new PedidoRequestDTO("Pendiente", 150.99, 1L, "Calle Falsa 123", "Tarjeta de Crédito");
    }

    @Test
    void testObtenerTodosLosPedidos() throws Exception {
        when(pedidoService.obtenerTodosLosPedidos()).thenReturn(Arrays.asList(responseDTO));

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("Pendiente"));
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