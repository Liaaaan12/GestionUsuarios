package GestionUsuarios.GestionUsuarios.service;

// Corrected DTO imports
import GestionUsuarios.GestionUsuarios.DTO.AdministradorRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.AdministradorResponseDTO;
import GestionUsuarios.GestionUsuarios.Mapper.AdministradorMapper;
// Corrected exception import (assuming exception is in this package structure)
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
// Corrected model imports (assuming model is in this package structure)
import GestionUsuarios.GestionUsuarios.model.Administrador;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
// Corrected repository imports (assuming repository is in this package structure)
import GestionUsuarios.GestionUsuarios.repository.AdministradorRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.security.crypto.password.PasswordEncoder; // Si usas Spring Security para contraseñas

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private AdministradorMapper administradorMapper;

    // @Autowired
    // private PasswordEncoder passwordEncoder; // Descomentar si usas Spring Security

    @Transactional(readOnly = true)
    public List<AdministradorResponseDTO> obtenerTodosLosAdministradores() {
        return administradorRepository.findAll()
                .stream()
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

        // Hashear contraseña
        // administrador.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // Por ahora, se guarda la contraseña tal como viene del DTO (mapeada por AdministradorMapper)

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

        // Si la contraseña se actualiza en el DTO, y el mapper ya la puso, se debe re-hashear aquí si es necesario.
        // Si el mapper no actualiza la contraseña a menos que se provea una nueva, y se hashea allí, está bien.
        // Asegurar que la contraseña se hashea si se cambia:
        // if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
        //    administradorExistente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // }

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