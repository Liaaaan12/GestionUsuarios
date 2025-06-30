package GestionUsuarios.GestionUsuarios.controller;

import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaResponseDTO;
import GestionUsuarios.GestionUsuarios.service.GerenteTiendaService;
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
 * Controlador REST para gestionar las operaciones relacionadas con los gerentes de tienda.
 */
@RestController
@RequestMapping("/gerentes-tienda")
@Tag(name = "Gestión de Gerentes de Tienda", description = "API para las operaciones CRUD de los gerentes de tienda.")
public class GerenteTiendaController {

    @Autowired
    private GerenteTiendaService gerenteTiendaService;

    /**
     * Endpoint para obtener una lista de todos los gerentes de tienda.
     * @return ResponseEntity con una lista de GerenteTiendaResponseDTO y estado HTTP 200 (OK).
     */
    @Operation(summary = "Obtener todos los gerentes de tienda", description = "Devuelve una lista de todos los gerentes de tienda registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de gerentes obtenida exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = GerenteTiendaResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<GerenteTiendaResponseDTO>> obtenerTodosLosGerentes() {
        return ResponseEntity.ok(gerenteTiendaService.obtenerTodosLosGerentes());
    }

    /**
     * Endpoint para obtener un gerente de tienda específico por su ID.
     * @param id El ID del gerente a buscar.
     * @return ResponseEntity con el GerenteTiendaResponseDTO encontrado y estado HTTP 200 (OK).
     */
    @Operation(summary = "Obtener un gerente de tienda por ID", description = "Busca un gerente de tienda específico utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gerente de tienda encontrado",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = GerenteTiendaResponseDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Gerente de tienda no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<GerenteTiendaResponseDTO> obtenerGerentePorId(
            @Parameter(description = "ID del gerente de tienda a obtener.", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(gerenteTiendaService.obtenerGerentePorId(id));
    }

    /**
     * Endpoint para crear un nuevo gerente de tienda.
     * @param requestDTO El DTO con la información del gerente a crear.
     * @return ResponseEntity con el GerenteTiendaResponseDTO creado y estado HTTP 201 (Created).
     */
    @Operation(summary = "Crear un nuevo gerente de tienda", description = "Registra un nuevo gerente de tienda.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Gerente de tienda creado exitosamente",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = GerenteTiendaResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<GerenteTiendaResponseDTO> crearGerente(
            @Parameter(description = "Datos del nuevo gerente de tienda.", required = true) @Valid @RequestBody GerenteTiendaRequestDTO requestDTO) {
        GerenteTiendaResponseDTO nuevoGerente = gerenteTiendaService.crearGerente(requestDTO);
        return new ResponseEntity<>(nuevoGerente, HttpStatus.CREATED);
    }

    /**
     * Endpoint para actualizar un gerente de tienda existente.
     * @param id El ID del gerente a actualizar.
     * @param requestDTO El DTO con los nuevos datos para el gerente.
     * @return ResponseEntity con el GerenteTiendaResponseDTO actualizado y estado HTTP 200 (OK).
     */
    @Operation(summary = "Actualizar un gerente de tienda", description = "Modifica los datos de un gerente de tienda existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gerente de tienda actualizado exitosamente",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = GerenteTiendaResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Gerente de tienda no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<GerenteTiendaResponseDTO> actualizarGerente(
            @Parameter(description = "ID del gerente a actualizar.", required = true) @PathVariable Long id,
            @Parameter(description = "Nuevos datos para el gerente.", required = true) @Valid @RequestBody GerenteTiendaRequestDTO requestDTO) {
        GerenteTiendaResponseDTO gerenteActualizado = gerenteTiendaService.actualizarGerente(id, requestDTO);
        return ResponseEntity.ok(gerenteActualizado);
    }

    /**
     * Endpoint para eliminar un gerente de tienda por su ID.
     * @param id El ID del gerente a eliminar.
     * @return ResponseEntity con estado HTTP 204 (No Content).
     */
    @Operation(summary = "Eliminar un gerente de tienda", description = "Elimina un gerente de tienda del sistema por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Gerente de tienda eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Gerente de tienda no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGerente(
            @Parameter(description = "ID del gerente a eliminar.", required = true) @PathVariable Long id) {
        gerenteTiendaService.eliminarGerente(id);
        return ResponseEntity.noContent().build();
    }
}