package GestionUsuarios.GestionUsuarios.controller;

import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasResponseDTO;
import GestionUsuarios.GestionUsuarios.service.EmpleadoVentasService;
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
@RequestMapping("/empleados-ventas")
public class EmpleadoVentasController {

    private final EmpleadoVentasService empleadoVentasService;

    public EmpleadoVentasController(EmpleadoVentasService empleadoVentasService) {
        this.empleadoVentasService = empleadoVentasService;
    }

    @GetMapping
    public CollectionModel<EntityModel<EmpleadoVentasResponseDTO>> obtenerTodosLosEmpleadosVentas() {
        List<EntityModel<EmpleadoVentasResponseDTO>> empleados = empleadoVentasService.obtenerTodosLosEmpleadosVentas().stream()
                .map(empleado -> EntityModel.of(empleado,
                        linkTo(methodOn(EmpleadoVentasController.class).obtenerEmpleadoVentasPorId(empleado.getId())).withSelfRel(),
                        linkTo(methodOn(EmpleadoVentasController.class).obtenerTodosLosEmpleadosVentas()).withRel("empleados-ventas")))
                .collect(Collectors.toList());
        return CollectionModel.of(empleados, linkTo(methodOn(EmpleadoVentasController.class).obtenerTodosLosEmpleadosVentas()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<EmpleadoVentasResponseDTO> obtenerEmpleadoVentasPorId(@PathVariable Long id) {
        EmpleadoVentasResponseDTO empleado = empleadoVentasService.obtenerEmpleadoVentasPorId(id);
        return EntityModel.of(empleado,
                linkTo(methodOn(EmpleadoVentasController.class).obtenerEmpleadoVentasPorId(id)).withSelfRel(),
                linkTo(methodOn(EmpleadoVentasController.class).obtenerTodosLosEmpleadosVentas()).withRel("empleados-ventas"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<EmpleadoVentasResponseDTO>> crearEmpleadoVentas(@Valid @RequestBody EmpleadoVentasRequestDTO requestDTO) {
        EmpleadoVentasResponseDTO nuevoEmpleado = empleadoVentasService.crearEmpleadoVentas(requestDTO);
        EntityModel<EmpleadoVentasResponseDTO> resource = EntityModel.of(nuevoEmpleado,
                linkTo(methodOn(EmpleadoVentasController.class).obtenerEmpleadoVentasPorId(nuevoEmpleado.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EmpleadoVentasResponseDTO>> actualizarEmpleadoVentas(@PathVariable Long id, @Valid @RequestBody EmpleadoVentasRequestDTO requestDTO) {
        EmpleadoVentasResponseDTO empleadoActualizado = empleadoVentasService.actualizarEmpleadoVentas(id, requestDTO);
        EntityModel<EmpleadoVentasResponseDTO> resource = EntityModel.of(empleadoActualizado,
                linkTo(methodOn(EmpleadoVentasController.class).obtenerEmpleadoVentasPorId(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleadoVentas(@PathVariable Long id) {
        empleadoVentasService.eliminarEmpleadoVentas(id);
        return ResponseEntity.noContent().build();
    }
}