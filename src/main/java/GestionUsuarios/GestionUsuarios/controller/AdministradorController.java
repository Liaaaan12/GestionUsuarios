package GestionUsuarios.GestionUsuarios.controller;

import GestionUsuarios.GestionUsuarios.DTO.AdministradorRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.AdministradorResponseDTO;
import GestionUsuarios.GestionUsuarios.service.AdministradorService;
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
@RequestMapping("/administradores")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @GetMapping
    public CollectionModel<EntityModel<AdministradorResponseDTO>> obtenerTodosLosAdministradores() {
        List<EntityModel<AdministradorResponseDTO>> administradores = administradorService.obtenerTodosLosAdministradores().stream()
                .map(admin -> EntityModel.of(admin,
                        linkTo(methodOn(AdministradorController.class).obtenerAdministradorPorId(admin.getId())).withSelfRel(),
                        linkTo(methodOn(AdministradorController.class).obtenerTodosLosAdministradores()).withRel("administradores")))
                .collect(Collectors.toList());
        return CollectionModel.of(administradores, linkTo(methodOn(AdministradorController.class).obtenerTodosLosAdministradores()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<AdministradorResponseDTO> obtenerAdministradorPorId(@PathVariable Long id) {
        AdministradorResponseDTO admin = administradorService.obtenerAdministradorPorId(id);
        return EntityModel.of(admin,
                linkTo(methodOn(AdministradorController.class).obtenerAdministradorPorId(id)).withSelfRel(),
                linkTo(methodOn(AdministradorController.class).obtenerTodosLosAdministradores()).withRel("administradores"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<AdministradorResponseDTO>> crearAdministrador(@Valid @RequestBody AdministradorRequestDTO requestDTO) {
        AdministradorResponseDTO nuevoAdmin = administradorService.crearAdministrador(requestDTO);
        EntityModel<AdministradorResponseDTO> resource = EntityModel.of(nuevoAdmin,
                linkTo(methodOn(AdministradorController.class).obtenerAdministradorPorId(nuevoAdmin.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<AdministradorResponseDTO>> actualizarAdministrador(@PathVariable Long id, @Valid @RequestBody AdministradorRequestDTO requestDTO) {
        AdministradorResponseDTO adminActualizado = administradorService.actualizarAdministrador(id, requestDTO);
        EntityModel<AdministradorResponseDTO> resource = EntityModel.of(adminActualizado,
                linkTo(methodOn(AdministradorController.class).obtenerAdministradorPorId(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdministrador(@PathVariable Long id) {
        administradorService.eliminarAdministrador(id);
        return ResponseEntity.noContent().build();
    }
}