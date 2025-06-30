package GestionUsuarios.GestionUsuarios.service;

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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar la lógica de negocio de los clientes.
 * Proporciona métodos para crear, leer, actualizar y eliminar clientes.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final ClienteMapper clienteMapper;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,
                          TipoUsuarioRepository tipoUsuarioRepository,
                          ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Obtiene una lista de todos los clientes.
     * @return Una lista de {@link ClienteResponseDTO} que representan a todos los clientes.
     */
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> obtenerTodosLosClientes() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un cliente por su ID.
     * @param id El ID del cliente a buscar.
     * @return El {@link ClienteResponseDTO} del cliente encontrado.
     * @throws ResourceNotFoundException si no se encuentra ningún cliente con el ID proporcionado.
     */
    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return clienteMapper.toResponseDTO(cliente);
    }

    /**
     * Crea un nuevo cliente en la base de datos.
     * @param requestDTO El DTO con la información del cliente a crear.
     * @return El {@link ClienteResponseDTO} del cliente recién creado.
     * @throws ResourceNotFoundException si el tipo de usuario especificado no existe.
     */
    @Transactional
    public ClienteResponseDTO crearCliente(ClienteRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        Cliente cliente = clienteMapper.toEntity(requestDTO);
        cliente.setTipoUsuario(tipoUsuario);
        cliente.setPassword(requestDTO.getPassword());

        Cliente nuevoCliente = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(nuevoCliente);
    }

    /**
     * Actualiza un cliente existente.
     * @param id El ID del cliente a actualizar.
     * @param requestDTO El DTO con los nuevos datos del cliente.
     * @return El {@link ClienteResponseDTO} del cliente actualizado.
     * @throws ResourceNotFoundException si el cliente o el tipo de usuario no se encuentran.
     */
    @Transactional
    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO requestDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId()));

        clienteMapper.updateEntityFromDto(requestDTO, clienteExistente);
        clienteExistente.setTipoUsuario(tipoUsuario);

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
             clienteExistente.setPassword(requestDTO.getPassword());
        }

        Cliente clienteActualizado = clienteRepository.save(clienteExistente);
        return clienteMapper.toResponseDTO(clienteActualizado);
    }

    /**
     * Elimina un cliente por su ID.
     * @param id El ID del cliente a eliminar.
     * @throws ResourceNotFoundException si el cliente no se encuentra.
     */
    @Transactional
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
        clienteRepository.deleteById(id);
    }
}