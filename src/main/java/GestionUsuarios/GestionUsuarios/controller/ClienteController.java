package GestionUsuarios.GestionUsuarios.controller;

import GestionUsuarios.GestionUsuarios.DTO.ClienteRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.ClienteResponseDTO;
import GestionUsuarios.GestionUsuarios.service.ClienteService;
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
 * Controlador REST para gestionar las operaciones relacionadas con los clientes.
 * Expone los endpoints para crear, leer, actualizar y eliminar clientes.
 * @author Lian
 * @version 5.0
 * @since 2025-06-30
 */
@RestController
@RequestMapping("/clientes")
@Tag(name = "Gestión de Clientes", description = "API para las operaciones CRUD de los clientes.")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Endpoint para obtener una lista de todos los clientes.
     * @return ResponseEntity con una lista de ClienteResponseDTO y estado HTTP 200 (OK).
     */
    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista de todos los clientes registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ClienteResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obtenerTodosLosClientes() {
        return ResponseEntity.ok(clienteService.obtenerTodosLosClientes());
    }

    /**
     * Endpoint para obtener un cliente específico por su ID.
     * @param id El ID del cliente a buscar.
     * @return ResponseEntity con el ClienteResponseDTO encontrado y estado HTTP 200 (OK).
     */
    @Operation(summary = "Obtener un cliente por ID", description = "Busca un cliente específico utilizando su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClienteResponseDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerClientePorId(
            @Parameter(description = "ID del cliente que se desea obtener.", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerClientePorId(id));
    }

    /**
     * Endpoint para crear un nuevo cliente.
     * @param requestDTO El DTO con la información del cliente a crear.
     * @return ResponseEntity con el ClienteResponseDTO del nuevo cliente y estado HTTP 201 (Created).
     */
    @Operation(summary = "Crear un nuevo cliente", description = "Registra un nuevo cliente en el sistema a partir de los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClienteResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(
            @Parameter(description = "Datos del nuevo cliente.", required = true) @Valid @RequestBody ClienteRequestDTO requestDTO) {
        ClienteResponseDTO nuevoCliente = clienteService.crearCliente(requestDTO);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    /**
     * Endpoint para actualizar un cliente existente.
     * @param id El ID del cliente a actualizar.
     * @param requestDTO El DTO con los nuevos datos para el cliente.
     * @return ResponseEntity con el ClienteResponseDTO actualizado y estado HTTP 200 (OK).
     */
    @Operation(summary = "Actualizar un cliente existente", description = "Modifica los datos de un cliente existente basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClienteResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(
            @Parameter(description = "ID del cliente a actualizar.", required = true) @PathVariable Long id,
            @Parameter(description = "Nuevos datos para el cliente.", required = true) @Valid @RequestBody ClienteRequestDTO requestDTO) {
        ClienteResponseDTO clienteActualizado = clienteService.actualizarCliente(id, requestDTO);
        return ResponseEntity.ok(clienteActualizado);
    }

    /**
     * Endpoint para eliminar un cliente por su ID.
     * @param id El ID del cliente a eliminar.
     * @return ResponseEntity con estado HTTP 204 (No Content).
     */
    @Operation(summary = "Eliminar un cliente", description = "Elimina un cliente del sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(
            @Parameter(description = "ID del cliente a eliminar.", required = true) @PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}