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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar la lógica de negocio de los empleados de ventas.
 */
@Service
public class EmpleadoVentasService {

    private final EmpleadoVentasRepository empleadoVentasRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final EmpleadoVentasMapper empleadoVentasMapper;

    @Autowired
    public EmpleadoVentasService(EmpleadoVentasRepository empleadoVentasRepository,
                                 TipoUsuarioRepository tipoUsuarioRepository,
                                 EmpleadoVentasMapper empleadoVentasMapper) {
        this.empleadoVentasRepository = empleadoVentasRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.empleadoVentasMapper = empleadoVentasMapper;
    }

    /**
     * Obtiene una lista de todos los empleados de ventas.
     * @return Una lista de {@link EmpleadoVentasResponseDTO}.
     */
    @Transactional(readOnly = true)
    public List<EmpleadoVentasResponseDTO> obtenerTodosLosEmpleadosVentas() {
        return empleadoVentasRepository.findAll().stream()
                .map(empleadoVentasMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un empleado de ventas por su ID.
     * @param id El ID del empleado.
     * @return El {@link EmpleadoVentasResponseDTO} del empleado encontrado.
     * @throws ResourceNotFoundException si no se encuentra el empleado.
     */
    @Transactional(readOnly = true)
    public EmpleadoVentasResponseDTO obtenerEmpleadoVentasPorId(Long id) {
        EmpleadoVentas empleado = empleadoVentasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + id));
        return empleadoVentasMapper.toResponseDTO(empleado);
    }

    /**
     * Crea un nuevo empleado de ventas.
     * @param requestDTO DTO con los datos del nuevo empleado.
     * @return El {@link EmpleadoVentasResponseDTO} del empleado creado.
     * @throws ResourceNotFoundException si el tipo de usuario no existe.
     */
    @Transactional
    public EmpleadoVentasResponseDTO crearEmpleadoVentas(EmpleadoVentasRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        EmpleadoVentas empleado = empleadoVentasMapper.toEntity(requestDTO);
        empleado.setTipoUsuario(tipoUsuario);
        empleado.setPassword(requestDTO.getPassword());

        EmpleadoVentas nuevoEmpleado = empleadoVentasRepository.save(empleado);
        return empleadoVentasMapper.toResponseDTO(nuevoEmpleado);
    }

    /**
     * Actualiza un empleado de ventas existente.
     * @param id El ID del empleado a actualizar.
     * @param requestDTO DTO con los nuevos datos.
     * @return El {@link EmpleadoVentasResponseDTO} del empleado actualizado.
     * @throws ResourceNotFoundException si el empleado o el tipo de usuario no se encuentran.
     */
    @Transactional
    public EmpleadoVentasResponseDTO actualizarEmpleadoVentas(Long id, EmpleadoVentasRequestDTO requestDTO) {
        EmpleadoVentas empleadoExistente = empleadoVentasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + id));

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        empleadoVentasMapper.updateEntityFromDto(requestDTO, empleadoExistente);
        empleadoExistente.setTipoUsuario(tipoUsuario);

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            empleadoExistente.setPassword(requestDTO.getPassword());
        }

        EmpleadoVentas empleadoActualizado = empleadoVentasRepository.save(empleadoExistente);
        return empleadoVentasMapper.toResponseDTO(empleadoActualizado);
    }

    /**
     * Elimina un empleado de ventas por su ID.
     * @param id El ID del empleado a eliminar.
     * @throws ResourceNotFoundException si el empleado no se encuentra.
     */
    @Transactional
    public void eliminarEmpleadoVentas(Long id) {
        if (!empleadoVentasRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + id);
        }
        empleadoVentasRepository.deleteById(id);
    }
}