package GestionUsuarios.GestionUsuarios.controller;


import com.usuarios.dto.GerenteTiendaRequestDTO;
import com.usuarios.dto.GerenteTiendaResponseDTO;
import com.usuarios.service.GerenteTiendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gerentes-tienda")
public class GerenteTiendaController {

    @Autowired
    private GerenteTiendaService gerenteTiendaService;

    @GetMapping
    public ResponseEntity<List<GerenteTiendaResponseDTO>> obtenerTodosLosGerentes() {
        return ResponseEntity.ok(gerenteTiendaService.obtenerTodosLosGerentes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GerenteTiendaResponseDTO> obtenerGerentePorId(@PathVariable Long id) {
        return ResponseEntity.ok(gerenteTiendaService.obtenerGerentePorId(id));
    }

    @PostMapping
    public ResponseEntity<GerenteTiendaResponseDTO> crearGerente(@Valid @RequestBody GerenteTiendaRequestDTO requestDTO) {
        GerenteTiendaResponseDTO nuevoGerente = gerenteTiendaService.crearGerente(requestDTO);
        return new ResponseEntity<>(nuevoGerente, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GerenteTiendaResponseDTO> actualizarGerente(@PathVariable Long id, @Valid @RequestBody GerenteTiendaRequestDTO requestDTO) {
        GerenteTiendaResponseDTO gerenteActualizado = gerenteTiendaService.actualizarGerente(id, requestDTO);
        return ResponseEntity.ok(gerenteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGerente(@PathVariable Long id) {
        gerenteTiendaService.eliminarGerente(id);
        return ResponseEntity.noContent().build();
    }
}