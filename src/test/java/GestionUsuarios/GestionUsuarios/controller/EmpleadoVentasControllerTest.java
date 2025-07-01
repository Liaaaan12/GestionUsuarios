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

import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.service.EmpleadoVentasService;

@WebMvcTest(EmpleadoVentasController.class)
public class EmpleadoVentasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoVentasService empleadoVentasService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmpleadoVentasResponseDTO responseDTO;
    private EmpleadoVentasRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new EmpleadoVentasResponseDTO(1L, "Empleado de Prueba", "empleado@test.com", "1998-11-20", "33444555-6", null, "2023-01-15", 500000.0);
        requestDTO = new EmpleadoVentasRequestDTO("Empleado de Prueba", "empleado@test.com", "password", "1998-11-20", "33444555-6", 2L, "2023-01-15", 500000.0);
    }

    @Test
    void testObtenerTodosLosEmpleadosVentas() throws Exception {
        when(empleadoVentasService.obtenerTodosLosEmpleadosVentas()).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/empleados-ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.empleadoVentasResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.empleadoVentasResponseDTOList[0].nombre", is("Empleado de Prueba")));
    }

    @Test
    void testObtenerEmpleadoVentasPorId_Exitoso() throws Exception {
        // Prueba para el camino feliz (200 OK)
        when(empleadoVentasService.obtenerEmpleadoVentasPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/empleados-ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Empleado de Prueba")))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testObtenerEmpleadoVentasPorId_NoEncontrado() throws Exception {
        // MODIFICACIÓN CLAVE: Prueba para el camino de error (404 Not Found)
        // Esto asegura que se cubra la línea que lanza la excepción en el servicio.
        Long idNoExistente = 99L;
        when(empleadoVentasService.obtenerEmpleadoVentasPorId(idNoExistente))
                .thenThrow(new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + idNoExistente));

        mockMvc.perform(get("/empleados-ventas/{id}", idNoExistente))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearEmpleadoVentas() throws Exception {
        when(empleadoVentasService.crearEmpleadoVentas(any(EmpleadoVentasRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/empleados-ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizarEmpleadoVentas() throws Exception {
        when(empleadoVentasService.actualizarEmpleadoVentas(eq(1L), any(EmpleadoVentasRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/empleados-ventas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarEmpleadoVentas() throws Exception {
        mockMvc.perform(delete("/empleados-ventas/1"))
                .andExpect(status().isNoContent());
    }
}