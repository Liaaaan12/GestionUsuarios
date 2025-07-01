package GestionUsuarios.GestionUsuarios.controller;

import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaResponseDTO;
import GestionUsuarios.GestionUsuarios.service.GerenteTiendaService;
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
@RequestMapping("/gerentes-tienda")
public class GerenteTiendaController {

    private final GerenteTiendaService gerenteTiendaService;

    public GerenteTiendaController(GerenteTiendaService gerenteTiendaService) {
        this.gerenteTiendaService = gerenteTiendaService;
    }

    @GetMapping
    public CollectionModel<EntityModel<GerenteTiendaResponseDTO>> obtenerTodosLosGerentes() {
        List<EntityModel<GerenteTiendaResponseDTO>> gerentes = gerenteTiendaService.obtenerTodosLosGerentes().stream()
                .map(gerente -> EntityModel.of(gerente,
                        linkTo(methodOn(GerenteTiendaController.class).obtenerGerentePorId(gerente.getId())).withSelfRel(),
                        linkTo(methodOn(GerenteTiendaController.class).obtenerTodosLosGerentes()).withRel("gerentes-tienda")))
                .collect(Collectors.toList());
        return CollectionModel.of(gerentes, linkTo(methodOn(GerenteTiendaController.class).obtenerTodosLosGerentes()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<GerenteTiendaResponseDTO> obtenerGerentePorId(@PathVariable Long id) {
        GerenteTiendaResponseDTO gerente = gerenteTiendaService.obtenerGerentePorId(id);
        return EntityModel.of(gerente,
                linkTo(methodOn(GerenteTiendaController.class).obtenerGerentePorId(id)).withSelfRel(),
                linkTo(methodOn(GerenteTiendaController.class).obtenerTodosLosGerentes()).withRel("gerentes-tienda"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<GerenteTiendaResponseDTO>> crearGerente(@Valid @RequestBody GerenteTiendaRequestDTO requestDTO) {
        GerenteTiendaResponseDTO nuevoGerente = gerenteTiendaService.crearGerente(requestDTO);
        EntityModel<GerenteTiendaResponseDTO> resource = EntityModel.of(nuevoGerente,
                linkTo(methodOn(GerenteTiendaController.class).obtenerGerentePorId(nuevoGerente.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<GerenteTiendaResponseDTO>> actualizarGerente(@PathVariable Long id, @Valid @RequestBody GerenteTiendaRequestDTO requestDTO) {
        GerenteTiendaResponseDTO gerenteActualizado = gerenteTiendaService.actualizarGerente(id, requestDTO);
        EntityModel<GerenteTiendaResponseDTO> resource = EntityModel.of(gerenteActualizado,
                linkTo(methodOn(GerenteTiendaController.class).obtenerGerentePorId(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGerente(@PathVariable Long id) {
        gerenteTiendaService.eliminarGerente(id);
        return ResponseEntity.noContent().build();
    }
}