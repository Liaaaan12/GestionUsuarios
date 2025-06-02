package GestionUsuarios.GestionUsuarios.service;

// Corrected imports
import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaResponseDTO;
import GestionUsuarios.GestionUsuarios.Mapper.GerenteTiendaMapper;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.model.GerenteTienda;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.GerenteTiendaRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GerenteTiendaService {

    @Autowired
    private GerenteTiendaRepository gerenteTiendaRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private GerenteTiendaMapper gerenteTiendaMapper;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<GerenteTiendaResponseDTO> obtenerTodosLosGerentes() {
        return gerenteTiendaRepository.findAll().stream()
                .map(gerenteTiendaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GerenteTiendaResponseDTO obtenerGerentePorId(Long id) {
        GerenteTienda gerente = gerenteTiendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gerente de Tienda no encontrado con id: " + id));
        return gerenteTiendaMapper.toResponseDTO(gerente);
    }

    @Transactional
    public GerenteTiendaResponseDTO crearGerente(GerenteTiendaRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        GerenteTienda gerente = gerenteTiendaMapper.toEntity(requestDTO);
        gerente.setTipoUsuario(tipoUsuario);
        // gerente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        GerenteTienda nuevoGerente = gerenteTiendaRepository.save(gerente);
        return gerenteTiendaMapper.toResponseDTO(nuevoGerente);
    }

    @Transactional
    public GerenteTiendaResponseDTO actualizarGerente(Long id, GerenteTiendaRequestDTO requestDTO) {
        GerenteTienda gerenteExistente = gerenteTiendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gerente de Tienda no encontrado con id: " + id));

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        gerenteTiendaMapper.updateEntityFromDto(requestDTO, gerenteExistente);
        gerenteExistente.setTipoUsuario(tipoUsuario);

        // if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
        //    gerenteExistente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // }

        GerenteTienda gerenteActualizado = gerenteTiendaRepository.save(gerenteExistente);
        return gerenteTiendaMapper.toResponseDTO(gerenteActualizado);
    }

    @Transactional
    public void eliminarGerente(Long id) {
        if (!gerenteTiendaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Gerente de Tienda no encontrado con id: " + id);
        }
        gerenteTiendaRepository.deleteById(id);
    }
}