package GestionUsuarios.GestionUsuarios.service;


import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.EmpleadoVentasMapper;
import GestionUsuarios.GestionUsuarios.model.EmpleadoVentas;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.EmpleadoVentasRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.security.crypto.password.PasswordEncoder; // Uncomment if using Spring Security for passwords

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoVentasService {

    private final EmpleadoVentasRepository empleadoVentasRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    // Corrected type for the mapper field
    private final EmpleadoVentasMapper empleadoVentasMapper;
    // private final PasswordEncoder passwordEncoder; // Uncomment if using Spring Security

    @Autowired
    public EmpleadoVentasService(EmpleadoVentasRepository empleadoVentasRepository,
                                 TipoUsuarioRepository tipoUsuarioRepository,
                                 EmpleadoVentasMapper empleadoVentasMapper
                                 /*, PasswordEncoder passwordEncoder */) { // Uncomment if using Spring Security
        this.empleadoVentasRepository = empleadoVentasRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.empleadoVentasMapper = empleadoVentasMapper;
        // this.passwordEncoder = passwordEncoder; // Uncomment if using Spring Security
    }

    @Transactional(readOnly = true)
    public List<EmpleadoVentasResponseDTO> obtenerTodosLosEmpleadosVentas() {
        return empleadoVentasRepository.findAll().stream()
                // Now correctly calls the method on EmpleadoVentasMapper instance
                .map(empleadoVentasMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmpleadoVentasResponseDTO obtenerEmpleadoVentasPorId(Long id) {
        EmpleadoVentas empleado = empleadoVentasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + id));
        // Now correctly calls the method on EmpleadoVentasMapper instance
        return empleadoVentasMapper.toResponseDTO(empleado);
    }

    @Transactional
    public EmpleadoVentasResponseDTO crearEmpleadoVentas(EmpleadoVentasRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        // Now correctly calls the method on EmpleadoVentasMapper instance
        EmpleadoVentas empleado = empleadoVentasMapper.toEntity(requestDTO);
        empleado.setTipoUsuario(tipoUsuario);

        // Handle password encoding if Spring Security is used
        // if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
        //     empleado.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // } else {
        //     // Or throw an exception if password is required
        //     throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        // }
        // For now, assuming password comes as is from DTO if not encoding
         empleado.setPassword(requestDTO.getPassword());


        EmpleadoVentas nuevoEmpleado = empleadoVentasRepository.save(empleado);
        // Now correctly calls the method on EmpleadoVentasMapper instance
        return empleadoVentasMapper.toResponseDTO(nuevoEmpleado);
    }

    @Transactional
    public EmpleadoVentasResponseDTO actualizarEmpleadoVentas(Long id, EmpleadoVentasRequestDTO requestDTO) {
        EmpleadoVentas empleadoExistente = empleadoVentasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + id));

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        // Now correctly calls the method on EmpleadoVentasMapper instance
        empleadoVentasMapper.updateEntityFromDto(requestDTO, empleadoExistente);
        empleadoExistente.setTipoUsuario(tipoUsuario); // Set/update the TipoUsuario association

        // Handle password update specifically if provided and Spring Security is used
        // if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
        //    empleadoExistente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // }
        // For now, assuming password comes as is from DTO if not encoding and mapper handles it
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            empleadoExistente.setPassword(requestDTO.getPassword()); // Or encode if security is set up
        }


        EmpleadoVentas empleadoActualizado = empleadoVentasRepository.save(empleadoExistente);
        // Now correctly calls the method on EmpleadoVentasMapper instance
        return empleadoVentasMapper.toResponseDTO(empleadoActualizado);
    }

    @Transactional
    public void eliminarEmpleadoVentas(Long id) {
        if (!empleadoVentasRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + id);
        }
        empleadoVentasRepository.deleteById(id);
    }

    // Removed the extraneous private toResponseDTO method
}
