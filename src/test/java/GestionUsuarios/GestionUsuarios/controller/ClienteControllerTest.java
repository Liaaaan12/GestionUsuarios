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

import GestionUsuarios.GestionUsuarios.DTO.ClienteRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.ClienteResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.service.ClienteService;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteResponseDTO responseDTO;
    private ClienteRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Corregido: Usar el constructor con todos los campos del DTO
        responseDTO = new ClienteResponseDTO(1L, "Cliente de Prueba", "cliente@test.com", "1995-05-10", "22333444-5", null, "Calle Falsa 123");
        requestDTO = new ClienteRequestDTO("Cliente de Prueba", "cliente@test.com", "password", "1995-05-10", "22333444-5", 1L, "Calle Falsa 123");
    }

    @Test
    void testObtenerTodosLosClientes() throws Exception {
        when(clienteService.obtenerTodosLosClientes()).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.clienteResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.clienteResponseDTOList[0].nombre", is("Cliente de Prueba")));
    }

    @Test
    void testObtenerClientePorId() throws Exception {
        when(clienteService.obtenerClientePorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
    
    @Test
    void testObtenerClientePorId_NoEncontrado() throws Exception {
        when(clienteService.obtenerClientePorId(99L)).thenThrow(new ResourceNotFoundException("Cliente no encontrado"));
        mockMvc.perform(get("/clientes/99")).andExpect(status().isNotFound());
    }

    @Test
    void testCrearCliente() throws Exception {
        when(clienteService.crearCliente(any(ClienteRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Cliente de Prueba")));
    }

    @Test
    void testActualizarCliente() throws Exception {
        when(clienteService.actualizarCliente(eq(1L), any(ClienteRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarCliente() throws Exception {
        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());
    }
}