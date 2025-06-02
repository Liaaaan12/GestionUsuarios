package GestionUsuarios.GestionUsuarios.service;


import com.usuarios.dto.EmpleadoVentasRequestDTO;
import com.usuarios.dto.EmpleadoVentasResponseDTO;
import com.usuarios.exception.ResourceNotFoundException;
import com.usuarios.mapper.EmpleadoVentasMapper;
import com.usuarios.model.EmpleadoVentas;
import com.usuarios.model.TipoUsuario;
import com.usuarios.repository.EmpleadoVentasRepository;
import com.usuarios.repository.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoVentasService {

    @Autowired
    private EmpleadoVentasRepository empleadoVentasRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private EmpleadoVentasMapper empleadoVentasMapper;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<EmpleadoVentasResponseDTO> obtenerTodosLosEmpleadosVentas() {
        return empleadoVentasRepository.findAll().stream()
                .map(empleadoVentasMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmpleadoVentasResponseDTO obtenerEmpleadoVentasPorId(Long id) {
        EmpleadoVentas empleado = empleadoVentasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + id));
        return empleadoVentasMapper.toResponseDTO(empleado);
    }

    @Transactional
    public EmpleadoVentasResponseDTO crearEmpleadoVentas(EmpleadoVentasRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        EmpleadoVentas empleado = empleadoVentasMapper.toEntity(requestDTO);
        empleado.setTipoUsuario(tipoUsuario);
        // empleado.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        EmpleadoVentas nuevoEmpleado = empleadoVentasRepository.save(empleado);
        return empleadoVentasMapper.toResponseDTO(nuevoEmpleado);
    }

    @Transactional
    public EmpleadoVentasResponseDTO actualizarEmpleadoVentas(Long id, EmpleadoVentasRequestDTO requestDTO) {
        EmpleadoVentas empleadoExistente = empleadoVentasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + id));

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        empleadoVentasMapper.updateEntityFromDto(requestDTO, empleadoExistente);
        empleadoExistente.setTipoUsuario(tipoUsuario);

        // if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
        //    empleadoExistente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // }

        EmpleadoVentas empleadoActualizado = empleadoVentasRepository.save(empleadoExistente);
        return empleadoVentasMapper.toResponseDTO(empleadoActualizado);
    }

    @Transactional
    public void eliminarEmpleadoVentas(Long id) {
        if (!empleadoVentasRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + id);
        }
        empleadoVentasRepository.deleteById(id);
    }
}