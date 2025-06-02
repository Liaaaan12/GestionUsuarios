package GestionUsuarios.GestionUsuarios.Mapper;

// Corrected imports
import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaResponseDTO;
import GestionUsuarios.GestionUsuarios.model.GerenteTienda;
// import GestionUsuarios.GestionUsuarios.model.TipoUsuario; // Already imported by TipoUsuarioMapper

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
// import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class GerenteTiendaMapper {

    @Autowired
    private TipoUsuarioMapper tipoUsuarioMapper;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    public GerenteTiendaResponseDTO toResponseDTO(GerenteTienda gerente) {
        if (gerente == null) {
            return null;
        }
        return new GerenteTiendaResponseDTO(
                gerente.getId(),
                gerente.getNombre(),
                gerente.getEmail(),
                gerente.getFechaNacimiento(),
                gerente.getRut(),
                tipoUsuarioMapper.toResponseDTO(gerente.getTipoUsuario()),
                gerente.getAnosExperiencia(),
                gerente.getTiendaAsignada()
        );
    }

    public GerenteTienda toEntity(GerenteTiendaRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        GerenteTienda gerente = new GerenteTienda();
        gerente.setNombre(requestDTO.getNombre());
        gerente.setEmail(requestDTO.getEmail());
        // gerente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        gerente.setPassword(requestDTO.getPassword()); // Temporal
        gerente.setFechaNacimiento(requestDTO.getFechaNacimiento());
        gerente.setRut(requestDTO.getRut());
        gerente.setAnosExperiencia(requestDTO.getAnosExperiencia());
        gerente.setTiendaAsignada(requestDTO.getTiendaAsignada());
        // TipoUsuario se establece en el servicio
        return gerente;
    }

    public void updateEntityFromDto(GerenteTiendaRequestDTO requestDTO, GerenteTienda gerente) {
        if (requestDTO == null || gerente == null) {
            return;
        }
        gerente.setNombre(requestDTO.getNombre());
        gerente.setEmail(requestDTO.getEmail());
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            // gerente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
            gerente.setPassword(requestDTO.getPassword()); // Temporal
        }
        gerente.setFechaNacimiento(requestDTO.getFechaNacimiento());
        gerente.setRut(requestDTO.getRut());
        gerente.setAnosExperiencia(requestDTO.getAnosExperiencia());
        gerente.setTiendaAsignada(requestDTO.getTiendaAsignada());
        // TipoUsuario se actualiza en el servicio
    }
}