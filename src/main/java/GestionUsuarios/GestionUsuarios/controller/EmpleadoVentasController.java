package GestionUsuarios.GestionUsuarios.controller;


import com.usuarios.dto.EmpleadoVentasRequestDTO;
import com.usuarios.dto.EmpleadoVentasResponseDTO;
import com.usuarios.service.EmpleadoVentasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empleados-ventas")
public class EmpleadoVentasController {

    @Autowired
    private EmpleadoVentasService empleadoVentasService;

    @GetMapping
    public ResponseEntity<List<EmpleadoVentasResponseDTO>> obtenerTodosLosEmpleadosVentas() {
        return ResponseEntity.ok(empleadoVentasService.obtenerTodosLosEmpleadosVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoVentasResponseDTO> obtenerEmpleadoVentasPorId(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoVentasService.obtenerEmpleadoVentasPorId(id));
    }

    @PostMapping
    public ResponseEntity<EmpleadoVentasResponseDTO> crearEmpleadoVentas(@Valid @RequestBody EmpleadoVentasRequestDTO requestDTO) {
        EmpleadoVentasResponseDTO nuevoEmpleado = empleadoVentasService.crearEmpleadoVentas(requestDTO);
        return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoVentasResponseDTO> actualizarEmpleadoVentas(@PathVariable Long id, @Valid @RequestBody EmpleadoVentasRequestDTO requestDTO) {
        EmpleadoVentasResponseDTO empleadoActualizado = empleadoVentasService.actualizarEmpleadoVentas(id, requestDTO);
        return ResponseEntity.ok(empleadoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleadoVentas(@PathVariable Long id) {
        empleadoVentasService.eliminarEmpleadoVentas(id);
        return ResponseEntity.noContent().build();
    }
}