package GestionUsuarios.GestionUsuarios.controller;

import GestionUsuarios.GestionUsuarios.DTO.PedidoRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.PedidoResponseDTO;
import GestionUsuarios.GestionUsuarios.service.PedidoService;
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
 * Controlador REST para gestionar las operaciones relacionadas con los pedidos.
 */
@RestController
@RequestMapping("/pedidos")
@Tag(name = "Gestión de Pedidos", description = "API para las operaciones CRUD de los pedidos de los clientes.")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * Endpoint para obtener una lista de todos los pedidos.
     * @return ResponseEntity con una lista de PedidoResponseDTO y estado HTTP 200 (OK).
     */
    @Operation(summary = "Obtener todos los pedidos", description = "Devuelve una lista de todos los pedidos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PedidoResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> obtenerTodosLosPedidos() {
        return ResponseEntity.ok(pedidoService.obtenerTodosLosPedidos());
    }

    /**
     * Endpoint para obtener un pedido específico por su ID.
     * @param id El ID del pedido a buscar.
     * @return ResponseEntity con el PedidoResponseDTO encontrado y estado HTTP 200 (OK).
     */
    @Operation(summary = "Obtener un pedido por ID", description = "Busca un pedido específico utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PedidoResponseDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtenerPedidoPorId(
            @Parameter(description = "ID del pedido a obtener.", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    /**
     * Endpoint para crear un nuevo pedido.
     * @param requestDTO El DTO con la información del pedido a crear.
     * @return ResponseEntity con el PedidoResponseDTO creado y estado HTTP 201 (Created).
     */
    @Operation(summary = "Crear un nuevo pedido", description = "Registra un nuevo pedido para un cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PedidoResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente asociado al pedido no encontrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crearPedido(
            @Parameter(description = "Datos del nuevo pedido.", required = true) @Valid @RequestBody PedidoRequestDTO requestDTO) {
        PedidoResponseDTO nuevoPedido = pedidoService.crearPedido(requestDTO);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    /**
     * Endpoint para actualizar un pedido existente.
     * @param id El ID del pedido a actualizar.
     * @param requestDTO El DTO con los nuevos datos para el pedido.
     * @return ResponseEntity con el PedidoResponseDTO actualizado y estado HTTP 200 (OK).
     */
    @Operation(summary = "Actualizar un pedido", description = "Modifica los datos de un pedido existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado exitosamente",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PedidoResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido o cliente no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> actualizarPedido(
            @Parameter(description = "ID del pedido a actualizar.", required = true) @PathVariable Long id,
            @Parameter(description = "Nuevos datos para el pedido.", required = true) @Valid @RequestBody PedidoRequestDTO requestDTO) {
        PedidoResponseDTO pedidoActualizado = pedidoService.actualizarPedido(id, requestDTO);
        return ResponseEntity.ok(pedidoActualizado);
    }

    /**
     * Endpoint para eliminar un pedido por su ID.
     * @param id El ID del pedido a eliminar.
     * @return ResponseEntity con estado HTTP 204 (No Content).
     */
    @Operation(summary = "Eliminar un pedido", description = "Elimina un pedido del sistema por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(
            @Parameter(description = "ID del pedido a eliminar.", required = true) @PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}