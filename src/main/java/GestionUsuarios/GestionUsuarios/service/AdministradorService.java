package GestionUsuarios.GestionUsuarios.service;

import GestionUsuarios.GestionUsuarios.DTO.AdministradorRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.AdministradorResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.AdministradorMapper;
import GestionUsuarios.GestionUsuarios.model.Administrador;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.AdministradorRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar la lógica de negocio de los administradores.
 * Proporciona métodos para crear, leer, actualizar y eliminar administradores.
 */
@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private AdministradorMapper administradorMapper;

    /**
     * Obtiene una lista de todos los administradores.
     * @return Una lista de {@link AdministradorResponseDTO} que representan a todos los administradores.
     */
    @Transactional(readOnly = true)
    public List<AdministradorResponseDTO> obtenerTodosLosAdministradores() {
        return administradorRepository.findAll()
                .stream()
                .map(administradorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un administrador por su ID.
     * @param id El ID del administrador a buscar.
     * @return El {@link AdministradorResponseDTO} del administrador encontrado.
     * @throws ResourceNotFoundException si no se encuentra ningún administrador con el ID proporcionado.
     */
    @Transactional(readOnly = true)
    public AdministradorResponseDTO obtenerAdministradorPorId(Long id) {
        Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado con id: " + id));
        return administradorMapper.toResponseDTO(administrador);
    }

    /**
     * Crea un nuevo administrador en la base de datos.
     * @param requestDTO El DTO con la información del administrador a crear.
     * @return El {@link AdministradorResponseDTO} del administrador recién creado.
     * @throws ResourceNotFoundException si el tipo de usuario especificado no existe.
     */
    @Transactional
    public AdministradorResponseDTO crearAdministrador(AdministradorRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        Administrador administrador = administradorMapper.toEntity(requestDTO);
        administrador.setTipoUsuario(tipoUsuario);

        Administrador nuevoAdministrador = administradorRepository.save(administrador);
        return administradorMapper.toResponseDTO(nuevoAdministrador);
    }

    /**
     * Actualiza un administrador existente.
     * @param id El ID del administrador a actualizar.
     * @param requestDTO El DTO con los nuevos datos del administrador.
     * @return El {@link AdministradorResponseDTO} del administrador actualizado.
     * @throws ResourceNotFoundException si el administrador o el tipo de usuario no se encuentran.
     */
    @Transactional
    public AdministradorResponseDTO actualizarAdministrador(Long id, AdministradorRequestDTO requestDTO) {
        Administrador administradorExistente = administradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado con id: " + id));

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        administradorMapper.updateEntityFromDto(requestDTO, administradorExistente);
        administradorExistente.setTipoUsuario(tipoUsuario);

        Administrador administradorActualizado = administradorRepository.save(administradorExistente);
        return administradorMapper.toResponseDTO(administradorActualizado);
    }

    /**
     * Elimina un administrador por su ID.
     * @param id El ID del administrador a eliminar.
     * @throws ResourceNotFoundException si el administrador no se encuentra.
     */
    @Transactional
    public void eliminarAdministrador(Long id) {
        if (!administradorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Administrador no encontrado con id: " + id);
        }
        administradorRepository.deleteById(id);
    }
}