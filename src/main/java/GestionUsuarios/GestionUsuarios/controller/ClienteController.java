package GestionUsuarios.GestionUsuarios.controller;

import GestionUsuarios.GestionUsuarios.DTO.ClienteRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.ClienteResponseDTO;
import GestionUsuarios.GestionUsuarios.service.ClienteService;
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
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public CollectionModel<EntityModel<ClienteResponseDTO>> obtenerTodosLosClientes() {
        List<EntityModel<ClienteResponseDTO>> clientes = clienteService.obtenerTodosLosClientes().stream()
                .map(cliente -> EntityModel.of(cliente,
                        linkTo(methodOn(ClienteController.class).obtenerClientePorId(cliente.getId())).withSelfRel(),
                        linkTo(methodOn(ClienteController.class).obtenerTodosLosClientes()).withRel("clientes")))
                .collect(Collectors.toList());
        return CollectionModel.of(clientes, linkTo(methodOn(ClienteController.class).obtenerTodosLosClientes()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<ClienteResponseDTO> obtenerClientePorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.obtenerClientePorId(id);
        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteController.class).obtenerClientePorId(id)).withSelfRel(),
                linkTo(methodOn(ClienteController.class).obtenerTodosLosClientes()).withRel("clientes"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ClienteResponseDTO>> crearCliente(@Valid @RequestBody ClienteRequestDTO requestDTO) {
        ClienteResponseDTO nuevoCliente = clienteService.crearCliente(requestDTO);
        EntityModel<ClienteResponseDTO> resource = EntityModel.of(nuevoCliente,
                linkTo(methodOn(ClienteController.class).obtenerClientePorId(nuevoCliente.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ClienteResponseDTO>> actualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO requestDTO) {
        ClienteResponseDTO clienteActualizado = clienteService.actualizarCliente(id, requestDTO);
        EntityModel<ClienteResponseDTO> resource = EntityModel.of(clienteActualizado,
                linkTo(methodOn(ClienteController.class).obtenerClientePorId(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}