package GestionUsuarios.GestionUsuarios.service;

// Corrected imports
import GestionUsuarios.GestionUsuarios.DTO.ClienteRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.ClienteResponseDTO;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.mapper.ClienteMapper;
import GestionUsuarios.GestionUsuarios.model.Cliente;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.ClienteRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

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
        // La contraseña ya debería estar "seteada" (potencialmente hasheada) por el mapper si se implementa allí,
        // o se hashea aquí si el mapper solo la pasa en texto plano.
        // Ejemplo: cliente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        Cliente nuevoCliente = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(nuevoCliente);
    }

    @Transactional
    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO requestDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        clienteMapper.updateEntityFromDto(requestDTO, clienteExistente);
        clienteExistente.setTipoUsuario(tipoUsuario);

        // Ejemplo de re-hasheo si la contraseña cambió y el mapper la actualizó:
        // if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
        //    clienteExistente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // }

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