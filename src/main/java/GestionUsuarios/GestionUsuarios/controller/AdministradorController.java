package GestionUsuarios.GestionUsuarios.controller;


import GestionUsuarios.GestionUsuarios.DTO.AdministradorRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.AdministradorResponseDTO;
import GestionUsuarios.GestionUsuarios.service.AdministradorService;
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
 * Controlador REST para gestionar las operaciones relacionadas con los administradores.
 * Expone los endpoints para crear, leer, actualizar y eliminar administradores.
 */
@RestController
@RequestMapping("/administradores")
@Tag(name = "Gestión de Administradores", description = "API para las operaciones CRUD de los administradores del sistema.")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    /**
     * Endpoint para obtener una lista de todos los administradores.
     * @return ResponseEntity con una lista de AdministradorResponseDTO y estado HTTP 200 (OK).
     */
    @Operation(summary = "Obtener todos los administradores", description = "Devuelve una lista de todos los administradores registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de administradores obtenida exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = AdministradorResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<AdministradorResponseDTO>> obtenerTodosLosAdministradores() {
        return ResponseEntity.ok(administradorService.obtenerTodosLosAdministradores());
    }

    /**
     * Endpoint para obtener un administrador específico por su ID.
     * @param id El ID del administrador a buscar.
     * @return ResponseEntity con el AdministradorResponseDTO encontrado y estado HTTP 200 (OK).
     */
    @Operation(summary = "Obtener un administrador por ID", description = "Busca un administrador específico utilizando su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrador encontrado",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdministradorResponseDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Administrador no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AdministradorResponseDTO> obtenerAdministradorPorId(
            @Parameter(description = "ID del administrador que se desea obtener.", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(administradorService.obtenerAdministradorPorId(id));
    }

    /**
     * Endpoint para crear un nuevo administrador.
     * @param requestDTO El DTO con la información del administrador a crear.
     * @return ResponseEntity con el AdministradorResponseDTO del nuevo administrador y estado HTTP 201 (Created).
     */
    @Operation(summary = "Crear un nuevo administrador", description = "Registra un nuevo administrador en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrador creado exitosamente",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdministradorResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AdministradorResponseDTO> crearAdministrador(
            @Parameter(description = "Datos del nuevo administrador.", required = true) @Valid @RequestBody AdministradorRequestDTO requestDTO) {
        AdministradorResponseDTO nuevoAdministrador = administradorService.crearAdministrador(requestDTO);
        return new ResponseEntity<>(nuevoAdministrador, HttpStatus.CREATED);
    }

    /**
     * Endpoint para actualizar un administrador existente.
     * @param id El ID del administrador a actualizar.
     * @param requestDTO El DTO con los nuevos datos para el administrador.
     * @return ResponseEntity con el AdministradorResponseDTO actualizado y estado HTTP 200 (OK).
     */
    @Operation(summary = "Actualizar un administrador existente", description = "Modifica los datos de un administrador existente basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrador actualizado exitosamente",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdministradorResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Administrador no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AdministradorResponseDTO> actualizarAdministrador(
            @Parameter(description = "ID del administrador a actualizar.", required = true) @PathVariable Long id,
            @Parameter(description = "Nuevos datos para el administrador.", required = true) @Valid @RequestBody AdministradorRequestDTO requestDTO) {
        AdministradorResponseDTO administradorActualizado = administradorService.actualizarAdministrador(id, requestDTO);
        return ResponseEntity.ok(administradorActualizado);
    }

    /**
     * Endpoint para eliminar un administrador por su ID.
     * @param id El ID del administrador a eliminar.
     * @return ResponseEntity con estado HTTP 204 (No Content).
     */
    @Operation(summary = "Eliminar un administrador", description = "Elimina un administrador del sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Administrador eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Administrador no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdministrador(
            @Parameter(description = "ID del administrador a eliminar.", required = true) @PathVariable Long id) {
        administradorService.eliminarAdministrador(id);
        return ResponseEntity.noContent().build();
    }
}