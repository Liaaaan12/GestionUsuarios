package GestionUsuarios.GestionUsuarios.service;

import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.EmpleadoVentasMapper;
import GestionUsuarios.GestionUsuarios.model.EmpleadoVentas;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.EmpleadoVentasRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoVentasService {

    private final EmpleadoVentasRepository empleadoVentasRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final EmpleadoVentasMapper empleadoVentasMapper;

    public EmpleadoVentasService(EmpleadoVentasRepository empleadoVentasRepository,
                                 TipoUsuarioRepository tipoUsuarioRepository,
                                 EmpleadoVentasMapper empleadoVentasMapper) {
        this.empleadoVentasRepository = empleadoVentasRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.empleadoVentasMapper = empleadoVentasMapper;
    }

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