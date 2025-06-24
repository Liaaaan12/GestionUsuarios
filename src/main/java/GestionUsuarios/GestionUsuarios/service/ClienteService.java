package GestionUsuarios.GestionUsuarios.service;

// Corrected DTO imports to use uppercase "DTO"
import GestionUsuarios.GestionUsuarios.DTO.ClienteRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.ClienteResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.ClienteMapper; // Assuming 'mapper' is the standardized package name
import GestionUsuarios.GestionUsuarios.model.Cliente;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.ClienteRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.security.crypto.password.PasswordEncoder; // Uncomment if using Spring Security for passwords

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final ClienteMapper clienteMapper;
    // private final PasswordEncoder passwordEncoder; // Uncomment if using Spring Security

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,
                          TipoUsuarioRepository tipoUsuarioRepository,
                          ClienteMapper clienteMapper
                          /*, PasswordEncoder passwordEncoder */) { // Uncomment if using Spring Security
        this.clienteRepository = clienteRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.clienteMapper = clienteMapper;
        // this.passwordEncoder = passwordEncoder; // Uncomment if using Spring Security
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> obtenerTodosLosClientes() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional
    public ClienteResponseDTO crearCliente(ClienteRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        Cliente cliente = clienteMapper.toEntity(requestDTO);
        cliente.setTipoUsuario(tipoUsuario);
        // Example of password encoding - ensure PasswordEncoder is configured and injected
        // if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
        //     cliente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // } else {
        //     // Handle cases where password might be empty if that's not allowed
        //     throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        // }
        // For now, assuming password comes as is from DTO if not encoding
        cliente.setPassword(requestDTO.getPassword());


        Cliente nuevoCliente = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(nuevoCliente);
    }

    @Transactional
    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO requestDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        // Update basic fields from DTO using the mapper
        clienteMapper.updateEntityFromDto(requestDTO, clienteExistente);
        clienteExistente.setTipoUsuario(tipoUsuario); // Set/update the TipoUsuario association

        // Handle password update specifically if provided
        // if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
        //    clienteExistente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // }
        // For now, assuming password comes as is from DTO if not encoding and mapper handles it
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
             clienteExistente.setPassword(requestDTO.getPassword()); // Or encode if security is set up
        }


        Cliente clienteActualizado = clienteRepository.save(clienteExistente);
        return clienteMapper.toResponseDTO(clienteActualizado);
    }

    @Transactional
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
        clienteRepository.deleteById(id);
    }
}
