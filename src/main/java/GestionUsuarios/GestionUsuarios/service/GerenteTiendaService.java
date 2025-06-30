package GestionUsuarios.GestionUsuarios.service;

import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.GerenteTiendaMapper;
import GestionUsuarios.GestionUsuarios.model.GerenteTienda;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.GerenteTiendaRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar la lógica de negocio de los gerentes de tienda.
 */
@Service
public class GerenteTiendaService {

    @Autowired
    private GerenteTiendaRepository gerenteTiendaRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private GerenteTiendaMapper gerenteTiendaMapper;

    /**
     * Obtiene una lista de todos los gerentes de tienda.
     * @return Una lista de {@link GerenteTiendaResponseDTO}.
     */
    @Transactional(readOnly = true)
    public List<GerenteTiendaResponseDTO> obtenerTodosLosGerentes() {
        return gerenteTiendaRepository.findAll().stream()
                .map(gerenteTiendaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un gerente de tienda por su ID.
     * @param id El ID del gerente.
     * @return El {@link GerenteTiendaResponseDTO} del gerente encontrado.
     * @throws ResourceNotFoundException si no se encuentra el gerente.
     */
    @Transactional(readOnly = true)
    public GerenteTiendaResponseDTO obtenerGerentePorId(Long id) {
        GerenteTienda gerente = gerenteTiendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gerente de Tienda no encontrado con id: " + id));
        return gerenteTiendaMapper.toResponseDTO(gerente);
    }

    /**
     * Crea un nuevo gerente de tienda.
     * @param requestDTO DTO con los datos del nuevo gerente.
     * @return El {@link GerenteTiendaResponseDTO} del gerente creado.
     * @throws ResourceNotFoundException si el tipo de usuario no existe.
     */
    @Transactional
    public GerenteTiendaResponseDTO crearGerente(GerenteTiendaRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        GerenteTienda gerente = gerenteTiendaMapper.toEntity(requestDTO);
        gerente.setTipoUsuario(tipoUsuario);

        GerenteTienda nuevoGerente = gerenteTiendaRepository.save(gerente);
        return gerenteTiendaMapper.toResponseDTO(nuevoGerente);
    }

    /**
     * Actualiza un gerente de tienda existente.
     * @param id El ID del gerente a actualizar.
     * @param requestDTO DTO con los nuevos datos.
     * @return El {@link GerenteTiendaResponseDTO} del gerente actualizado.
     * @throws ResourceNotFoundException si el gerente o el tipo de usuario no se encuentran.
     */
    @Transactional
    public GerenteTiendaResponseDTO actualizarGerente(Long id, GerenteTiendaRequestDTO requestDTO) {
        GerenteTienda gerenteExistente = gerenteTiendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gerente de Tienda no encontrado con id: " + id));

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        gerenteTiendaMapper.updateEntityFromDto(requestDTO, gerenteExistente);
        gerenteExistente.setTipoUsuario(tipoUsuario);

        GerenteTienda gerenteActualizado = gerenteTiendaRepository.save(gerenteExistente);
        return gerenteTiendaMapper.toResponseDTO(gerenteActualizado);
    }

    /**
     * Elimina un gerente de tienda por su ID.
     * @param id El ID del gerente a eliminar.
     * @throws ResourceNotFoundException si el gerente no se encuentra.
     */
    @Transactional
    public void eliminarGerente(Long id) {
        if (!gerenteTiendaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Gerente de Tienda no encontrado con id: " + id);
        }
        gerenteTiendaRepository.deleteById(id);
    }
}