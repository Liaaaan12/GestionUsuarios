package GestionUsuarios.GestionUsuarios.mapper;

// Corrected DTO imports
import GestionUsuarios.GestionUsuarios.DTO.ClienteRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.ClienteResponseDTO;
// Corrected model imports (assuming model is in this package structure)
import GestionUsuarios.GestionUsuarios.model.Cliente;
// import GestionUsuarios.GestionUsuarios.model.TipoUsuario; // If needed directly

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
// import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class ClienteMapper {

    @Autowired
    private TipoUsuarioMapper tipoUsuarioMapper;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    // Si se incluyen pedidos, se necesitaría PedidoMapper
    // @Autowired
    // private PedidoMapper pedidoMapper;

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
        // if (cliente.getPedidos() != null) {
        //     dto.setPedidos(cliente.getPedidos().stream()
        //           .map(pedidoMapper::toResponseDTO)
        //           .collect(Collectors.toList()));
        // }
        return dto;
    }

    public Cliente toEntity(ClienteRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setNombre(requestDTO.getNombre());
        cliente.setEmail(requestDTO.getEmail());
        // cliente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        cliente.setPassword(requestDTO.getPassword()); // Temporal
        cliente.setFechaNacimiento(requestDTO.getFechaNacimiento());
        cliente.setRut(requestDTO.getRut());
        cliente.setDireccionEnvio(requestDTO.getDireccionEnvio());
        // TipoUsuario se establece en el servicio
        return cliente;
    }

    public void updateEntityFromDto(ClienteRequestDTO requestDTO, Cliente cliente) {
        if (requestDTO == null || cliente == null) {
            return;
        }
        cliente.setNombre(requestDTO.getNombre());
        cliente.setEmail(requestDTO.getEmail());
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            // cliente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
            cliente.setPassword(requestDTO.getPassword()); // Temporal
        }
        cliente.setFechaNacimiento(requestDTO.getFechaNacimiento());
        cliente.setRut(requestDTO.getRut());
        cliente.setDireccionEnvio(requestDTO.getDireccionEnvio());
        // TipoUsuario se actualiza en el servicio
    }
}