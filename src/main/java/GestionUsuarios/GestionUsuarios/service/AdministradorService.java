package GestionUsuarios.GestionUsuarios.service;

import GestionUsuarios.GestionUsuarios.DTO.AdministradorRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.AdministradorResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.AdministradorMapper;
import GestionUsuarios.GestionUsuarios.model.Administrador;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.AdministradorRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final AdministradorMapper administradorMapper;

    public AdministradorService(AdministradorRepository administradorRepository,
                                TipoUsuarioRepository tipoUsuarioRepository,
                                AdministradorMapper administradorMapper) {
        this.administradorRepository = administradorRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.administradorMapper = administradorMapper;
    }

    @Transactional(readOnly = true)
    public List<AdministradorResponseDTO> obtenerTodosLosAdministradores() {
        return administradorRepository.findAll().stream()
                .map(administradorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdministradorResponseDTO obtenerAdministradorPorId(Long id) {
        Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado con id: " + id));
        return administradorMapper.toResponseDTO(administrador);
    }

    @Transactional
    public AdministradorResponseDTO crearAdministrador(AdministradorRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        Administrador administrador = administradorMapper.toEntity(requestDTO);
        administrador.setTipoUsuario(tipoUsuario);

        Administrador nuevoAdministrador = administradorRepository.save(administrador);
        return administradorMapper.toResponseDTO(nuevoAdministrador);
    }

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

    @Transactional
    public void eliminarAdministrador(Long id) {
        if (!administradorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Administrador no encontrado con id: " + id);
        }
        administradorRepository.deleteById(id);
    }
}