package GestionUsuarios.GestionUsuarios.controller;


import GestionUsuarios.GestionUsuarios.DTO.AdministradorRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.AdministradorResponseDTO;
import GestionUsuarios.GestionUsuarios.service.AdministradorService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @GetMapping
    public ResponseEntity<List<AdministradorResponseDTO>> obtenerTodosLosAdministradores() {
        return ResponseEntity.ok(administradorService.obtenerTodosLosAdministradores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorResponseDTO> obtenerAdministradorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(administradorService.obtenerAdministradorPorId(id));
    }

    @PostMapping
    public ResponseEntity<AdministradorResponseDTO> crearAdministrador(@Valid @RequestBody AdministradorRequestDTO requestDTO) {
        AdministradorResponseDTO nuevoAdministrador = administradorService.crearAdministrador(requestDTO);
        return new ResponseEntity<>(nuevoAdministrador, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministradorResponseDTO> actualizarAdministrador(@PathVariable Long id, @Valid @RequestBody AdministradorRequestDTO requestDTO) {
        AdministradorResponseDTO administradorActualizado = administradorService.actualizarAdministrador(id, requestDTO);
        return ResponseEntity.ok(administradorActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdministrador(@PathVariable Long id) {
        administradorService.eliminarAdministrador(id);
        return ResponseEntity.noContent().build();
    }
}