package GestionUsuarios.GestionUsuarios.mapper;

import GestionUsuarios.GestionUsuarios.DTO.ClienteRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.ClienteResponseDTO;
import GestionUsuarios.GestionUsuarios.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
// import org.springframework.security.crypto.password.PasswordEncoder; // Para hashear contraseñas

@Component
public class ClienteMapper {

    @Autowired
    private TipoUsuarioMapper tipoUsuarioMapper;

    // @Autowired
    // private PasswordEncoder passwordEncoder; // Descomentar si usas Spring Security

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setFechaNacimiento(cliente.getFechaNacimiento());
        dto.setRut(cliente.getRut());
        dto.setTipoUsuario(tipoUsuarioMapper.toResponseDTO(cliente.getTipoUsuario()));
        dto.setDireccionEnvio(cliente.getDireccionEnvio());
        // La lista de pedidos se omitiría aquí a menos que se quiera una carga profunda por defecto.
        // dto.setPedidos(cliente.getPedidos().stream().map(pedidoMapper::toResponseDTO).collect(Collectors.toList())); // Necesitaría PedidoMapper
        return dto;
    }

    public Cliente toEntity(ClienteRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setNombre(requestDTO.getNombre());
        cliente.setEmail(requestDTO.getEmail());
        // La contraseña se debe hashear en el servicio antes de guardar
        // cliente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        cliente.setPassword(requestDTO.getPassword()); // Temporal: guardar como viene
        cliente.setFechaNacimiento(requestDTO.getFechaNacimiento());
        cliente.setRut(requestDTO.getRut());
        cliente.setDireccionEnvio(requestDTO.getDireccionEnvio());
        // TipoUsuario se establecerá en el servicio después de cargarlo por ID
        return cliente;
    }

    public void updateEntityFromDto(ClienteRequestDTO requestDTO, Cliente cliente) {
        if (requestDTO == null || cliente == null) {
            return;
        }
        cliente.setNombre(requestDTO.getNombre());
        cliente.setEmail(requestDTO.getEmail());

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            // La contraseña se debe hashear en el servicio antes de guardar
            // cliente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
            cliente.setPassword(requestDTO.getPassword()); // Temporal: guardar como viene
        }
        cliente.setFechaNacimiento(requestDTO.getFechaNacimiento());
        cliente.setRut(requestDTO.getRut());
        cliente.setDireccionEnvio(requestDTO.getDireccionEnvio());
        // TipoUsuario se actualizará en el servicio
    }
}