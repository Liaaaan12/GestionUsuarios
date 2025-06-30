package GestionUsuarios.GestionUsuarios.controller;

import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasResponseDTO;
import GestionUsuarios.GestionUsuarios.service.EmpleadoVentasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los empleados de ventas.
 */
@RestController
@RequestMapping("/empleados-ventas")
@Tag(name = "Gestión de Empleados de Ventas", description = "API para las operaciones CRUD de los empleados de ventas.")
public class EmpleadoVentasController {

    @Autowired
    private EmpleadoVentasService empleadoVentasService;

    /**
     * Endpoint para obtener una lista de todos los empleados de ventas.
     * @return ResponseEntity con una lista de EmpleadoVentasResponseDTO y estado HTTP 200 (OK).
     */
    @Operation(summary = "Obtener todos los empleados de ventas", description = "Devuelve una lista de todos los empleados de ventas registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = EmpleadoVentasResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<EmpleadoVentasResponseDTO>> obtenerTodosLosEmpleadosVentas() {
        return ResponseEntity.ok(empleadoVentasService.obtenerTodosLosEmpleadosVentas());
    }

    /**
     * Endpoint para obtener un empleado de ventas específico por su ID.
     * @param id El ID del empleado a buscar.
     * @return ResponseEntity con el EmpleadoVentasResponseDTO encontrado y estado HTTP 200 (OK).
     */
    @Operation(summary = "Obtener un empleado de ventas por ID", description = "Busca un empleado de ventas específico utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado de ventas encontrado",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EmpleadoVentasResponseDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Empleado de ventas no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoVentasResponseDTO> obtenerEmpleadoVentasPorId(
            @Parameter(description = "ID del empleado de ventas a obtener.", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(empleadoVentasService.obtenerEmpleadoVentasPorId(id));
    }

    /**
     * Endpoint para crear un nuevo empleado de ventas.
     * @param requestDTO El DTO con la información del empleado a crear.
     * @return ResponseEntity con el EmpleadoVentasResponseDTO creado y estado HTTP 201 (Created).
     */
    @Operation(summary = "Crear un nuevo empleado de ventas", description = "Registra un nuevo empleado de ventas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado de ventas creado exitosamente",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EmpleadoVentasResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EmpleadoVentasResponseDTO> crearEmpleadoVentas(
            @Parameter(description = "Datos del nuevo empleado de ventas.", required = true) @Valid @RequestBody EmpleadoVentasRequestDTO requestDTO) {
        EmpleadoVentasResponseDTO nuevoEmpleado = empleadoVentasService.crearEmpleadoVentas(requestDTO);
        return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
    }

    /**
     * Endpoint para actualizar un empleado de ventas existente.
     * @param id El ID del empleado a actualizar.
     * @param requestDTO El DTO con los nuevos datos para el empleado.
     * @return ResponseEntity con el EmpleadoVentasResponseDTO actualizado y estado HTTP 200 (OK).
     */
    @Operation(summary = "Actualizar un empleado de ventas", description = "Modifica los datos de un empleado de ventas existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado de ventas actualizado exitosamente",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EmpleadoVentasResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Empleado de ventas no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoVentasResponseDTO> actualizarEmpleadoVentas(
            @Parameter(description = "ID del empleado a actualizar.", required = true) @PathVariable Long id,
            @Parameter(description = "Nuevos datos para el empleado.", required = true) @Valid @RequestBody EmpleadoVentasRequestDTO requestDTO) {
        EmpleadoVentasResponseDTO empleadoActualizado = empleadoVentasService.actualizarEmpleadoVentas(id, requestDTO);
        return ResponseEntity.ok(empleadoActualizado);
    }

    /**
     * Endpoint para eliminar un empleado de ventas por su ID.
     * @param id El ID del empleado a eliminar.
     * @return ResponseEntity con estado HTTP 204 (No Content).
     */
    @Operation(summary = "Eliminar un empleado de ventas", description = "Elimina un empleado de ventas del sistema por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado de ventas eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Empleado de ventas no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleadoVentas(
            @Parameter(description = "ID del empleado a eliminar.", required = true) @PathVariable Long id) {
        empleadoVentasService.eliminarEmpleadoVentas(id);
        return ResponseEntity.noContent().build();
    }
}