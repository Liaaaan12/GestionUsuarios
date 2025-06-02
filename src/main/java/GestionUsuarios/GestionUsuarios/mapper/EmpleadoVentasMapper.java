package GestionUsuarios.GestionUsuarios.Mapper;

// Corrected imports
import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasResponseDTO;
import GestionUsuarios.GestionUsuarios.model.EmpleadoVentas;
// import GestionUsuarios.GestionUsuarios.model.TipoUsuario; // Already imported by TipoUsuarioMapper if used

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
// import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class EmpleadoVentasMapper {

    @Autowired
    private TipoUsuarioMapper tipoUsuarioMapper;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    public EmpleadoVentasResponseDTO toResponseDTO(EmpleadoVentas empleado) {
        if (empleado == null) {
            return null;
        }
        return new EmpleadoVentasResponseDTO(
                empleado.getId(),
                empleado.getNombre(),
                empleado.getEmail(),
                empleado.getFechaNacimiento(),
                empleado.getRut(),
                tipoUsuarioMapper.toResponseDTO(empleado.getTipoUsuario()),
                empleado.getFechaContratacion(),
                empleado.getSalario()
        );
    }

    public EmpleadoVentas toEntity(EmpleadoVentasRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        EmpleadoVentas empleado = new EmpleadoVentas();
        empleado.setNombre(requestDTO.getNombre());
        empleado.setEmail(requestDTO.getEmail());
        // empleado.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        empleado.setPassword(requestDTO.getPassword()); // Temporal
        empleado.setFechaNacimiento(requestDTO.getFechaNacimiento());
        empleado.setRut(requestDTO.getRut());
        empleado.setFechaContratacion(requestDTO.getFechaContratacion());
        empleado.setSalario(requestDTO.getSalario());
        // TipoUsuario se establece en el servicio
        return empleado;
    }

    public void updateEntityFromDto(EmpleadoVentasRequestDTO requestDTO, EmpleadoVentas empleado) {
        if (requestDTO == null || empleado == null) {
            return;
        }
        empleado.setNombre(requestDTO.getNombre());
        empleado.setEmail(requestDTO.getEmail());
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            // empleado.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
            empleado.setPassword(requestDTO.getPassword()); // Temporal
        }
        empleado.setFechaNacimiento(requestDTO.getFechaNacimiento());
        empleado.setRut(requestDTO.getRut());
        empleado.setFechaContratacion(requestDTO.getFechaContratacion());
        empleado.setSalario(requestDTO.getSalario());
        // TipoUsuario se actualiza en el servicio
    }
}