package GestionUsuarios.GestionUsuarios.controller;

import GestionUsuarios.GestionUsuarios.DTO.PedidoRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.PedidoResponseDTO;
import GestionUsuarios.GestionUsuarios.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public CollectionModel<EntityModel<PedidoResponseDTO>> obtenerTodosLosPedidos() {
        List<EntityModel<PedidoResponseDTO>> pedidos = pedidoService.obtenerTodosLosPedidos().stream()
                .map(pedido -> EntityModel.of(pedido,
                        linkTo(methodOn(PedidoController.class).obtenerPedidoPorId(pedido.getId())).withSelfRel(),
                        linkTo(methodOn(PedidoController.class).obtenerTodosLosPedidos()).withRel("pedidos")))
                .collect(Collectors.toList());
        return CollectionModel.of(pedidos, linkTo(methodOn(PedidoController.class).obtenerTodosLosPedidos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<PedidoResponseDTO> obtenerPedidoPorId(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.obtenerPedidoPorId(id);
        return EntityModel.of(pedido,
                linkTo(methodOn(PedidoController.class).obtenerPedidoPorId(id)).withSelfRel(),
                linkTo(methodOn(PedidoController.class).obtenerTodosLosPedidos()).withRel("pedidos"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<PedidoResponseDTO>> crearPedido(@Valid @RequestBody PedidoRequestDTO requestDTO) {
        PedidoResponseDTO nuevoPedido = pedidoService.crearPedido(requestDTO);
        EntityModel<PedidoResponseDTO> resource = EntityModel.of(nuevoPedido,
                linkTo(methodOn(PedidoController.class).obtenerPedidoPorId(nuevoPedido.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoResponseDTO>> actualizarPedido(@PathVariable Long id, @Valid @RequestBody PedidoRequestDTO requestDTO) {
        PedidoResponseDTO pedidoActualizado = pedidoService.actualizarPedido(id, requestDTO);
        EntityModel<PedidoResponseDTO> resource = EntityModel.of(pedidoActualizado,
                linkTo(methodOn(PedidoController.class).obtenerPedidoPorId(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}