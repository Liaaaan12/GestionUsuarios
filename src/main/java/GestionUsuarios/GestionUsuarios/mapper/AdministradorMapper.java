package GestionUsuarios.GestionUsuarios.mapper;

import com.GestionUsuarios.dto.AdministradorRequestDTO;
import com.GestionUsuarios.dto.AdministradorResponseDTO;
import com.GestionUsuarios.model.Administrador;
import com.GestionUsuarios.model.TipoUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
// import org.springframework.security.crypto.password.PasswordEncoder; // Para hashear contraseñas

@Component
public class AdministradorMapper {

    @Autowired
    private TipoUsuarioMapper tipoUsuarioMapper;

    // @Autowired
    // private PasswordEncoder passwordEncoder; // Descomentar si usas Spring Security

    public AdministradorResponseDTO toResponseDTO(Administrador administrador) {
        if (administrador == null) {
            return null;
        }
        return new AdministradorResponseDTO(
                administrador.getId(),
                administrador.getNombre(),
                administrador.getEmail(),
                administrador.getFechaNacimiento(),
                administrador.getRut(),
                tipoUsuarioMapper.toResponseDTO(administrador.getTipoUsuario())
        );
    }

    public Administrador toEntity(AdministradorRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        Administrador administrador = new Administrador();
        administrador.setNombre(requestDTO.getNombre());
        administrador.setEmail(requestDTO.getEmail());
        // La contraseña se debe hashear en el servicio antes de guardar
        // administrador.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        administrador.setPassword(requestDTO.getPassword()); // Temporal: guardar como viene
        administrador.setFechaNacimiento(requestDTO.getFechaNacimiento());
        administrador.setRut(requestDTO.getRut());

        // TipoUsuario se establecerá en el servicio después de cargarlo por ID
        return administrador;
    }

    public void updateEntityFromDto(AdministradorRequestDTO requestDTO, Administrador administrador) {
        if (requestDTO == null || administrador == null) {
            return;
        }
        administrador.setNombre(requestDTO.getNombre());
        administrador.setEmail(requestDTO.getEmail());

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            // La contraseña se debe hashear en el servicio antes de guardar
            // administrador.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
             administrador.setPassword(requestDTO.getPassword()); // Temporal: guardar como viene
        }
        administrador.setFechaNacimiento(requestDTO.getFechaNacimiento());
        administrador.setRut(requestDTO.getRut());
        // TipoUsuario se actualizará en el servicio
    }
}