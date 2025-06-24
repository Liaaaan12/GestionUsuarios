package GestionUsuarios.GestionUsuarios.service;

import GestionUsuarios.GestionUsuarios.DTO.AdministradorRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.AdministradorResponseDTO;
import GestionUsuarios.GestionUsuarios.mapper.AdministradorMapper;
import GestionUsuarios.GestionUsuarios.exception.ResourceNotFoundException;
import GestionUsuarios.GestionUsuarios.model.Administrador;
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;
import GestionUsuarios.GestionUsuarios.repository.AdministradorRepository;
import GestionUsuarios.GestionUsuarios.repository.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

// DTO imports for ClienteService
import GestionUsuarios.GestionUsuarios.DTO.ClienteRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.ClienteResponseDTO;
import GestionUsuarios.GestionUsuarios.mapper.ClienteMapper;
import GestionUsuarios.GestionUsuarios.model.Cliente;
import GestionUsuarios.GestionUsuarios.repository.ClienteRepository;

// DTO imports for EmpleadoVentasService
import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.EmpleadoVentasResponseDTO;
import GestionUsuarios.GestionUsuarios.mapper.EmpleadoVentasMapper;
import GestionUsuarios.GestionUsuarios.model.EmpleadoVentas;
import GestionUsuarios.GestionUsuarios.repository.EmpleadoVentasRepository;

// DTO imports for GerenteTiendaService
import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.GerenteTiendaResponseDTO;
import GestionUsuarios.GestionUsuarios.mapper.GerenteTiendaMapper;
import GestionUsuarios.GestionUsuarios.model.GerenteTienda;
import GestionUsuarios.GestionUsuarios.repository.GerenteTiendaRepository;

// DTO imports for PedidoService
import GestionUsuarios.GestionUsuarios.DTO.PedidoRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.PedidoResponseDTO;
import GestionUsuarios.GestionUsuarios.mapper.PedidoMapper;
import GestionUsuarios.GestionUsuarios.model.Pedido;
import GestionUsuarios.GestionUsuarios.repository.PedidoRepository;
import java.time.LocalDateTime;


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
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado con id: " + id)); //
        return administradorMapper.toResponseDTO(administrador);
    }

    @Transactional
    public AdministradorResponseDTO crearAdministrador(AdministradorRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId())); //

        Administrador administrador = administradorMapper.toEntity(requestDTO); //
        administrador.setTipoUsuario(tipoUsuario); //

        Administrador nuevoAdministrador = administradorRepository.save(administrador); //
        return administradorMapper.toResponseDTO(nuevoAdministrador);
    }

    @Transactional
    public AdministradorResponseDTO actualizarAdministrador(Long id, AdministradorRequestDTO requestDTO) {
        Administrador administradorExistente = administradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado con id: " + id)); //

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId())); //

        administradorMapper.updateEntityFromDto(requestDTO, administradorExistente); //
        administradorExistente.setTipoUsuario(tipoUsuario); //

        Administrador administradorActualizado = administradorRepository.save(administradorExistente); //
        return administradorMapper.toResponseDTO(administradorActualizado);
    }

    @Transactional
    public void eliminarAdministrador(Long id) {
        if (!administradorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Administrador no encontrado con id: " + id); //
        }
        administradorRepository.deleteById(id); //
    }
}


@Service
class ClienteService {

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
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id)); //
        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional
    public ClienteResponseDTO crearCliente(ClienteRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId())); //

        Cliente cliente = clienteMapper.toEntity(requestDTO); //
        cliente.setTipoUsuario(tipoUsuario); //
        cliente.setPassword(requestDTO.getPassword()); //


        Cliente nuevoCliente = clienteRepository.save(cliente); //
        return clienteMapper.toResponseDTO(nuevoCliente);
    }

    @Transactional
    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO requestDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id)); //

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId())); //

        clienteMapper.updateEntityFromDto(requestDTO, clienteExistente); //
        clienteExistente.setTipoUsuario(tipoUsuario); //
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
             clienteExistente.setPassword(requestDTO.getPassword()); //
        }


        Cliente clienteActualizado = clienteRepository.save(clienteExistente); //
        return clienteMapper.toResponseDTO(clienteActualizado);
    }

    @Transactional
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id); //
        }
        clienteRepository.deleteById(id); //
    }
}


@Service
class EmpleadoVentasService {

    private final EmpleadoVentasRepository empleadoVentasRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final EmpleadoVentasMapper empleadoVentasMapper;
    // private final PasswordEncoder passwordEncoder; // Uncomment if using Spring Security

    @Autowired
    public EmpleadoVentasService(EmpleadoVentasRepository empleadoVentasRepository,
                                 TipoUsuarioRepository tipoUsuarioRepository,
                                 EmpleadoVentasMapper empleadoVentasMapper
                                 /*, PasswordEncoder passwordEncoder */) { // Uncomment if using Spring Security
        this.empleadoVentasRepository = empleadoVentasRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.empleadoVentasMapper = empleadoVentasMapper;
        // this.passwordEncoder = passwordEncoder; // Uncomment if using Spring Security
    }

    @Transactional(readOnly = true)
    public List<EmpleadoVentasResponseDTO> obtenerTodosLosEmpleadosVentas() {
        return empleadoVentasRepository.findAll().stream()
                .map(empleadoVentasMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmpleadoVentasResponseDTO obtenerEmpleadoVentasPorId(Long id) {
        EmpleadoVentas empleado = empleadoVentasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + id)); //
        return empleadoVentasMapper.toResponseDTO(empleado);
    }

    @Transactional
    public EmpleadoVentasResponseDTO crearEmpleadoVentas(EmpleadoVentasRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId())); //

        EmpleadoVentas empleado = empleadoVentasMapper.toEntity(requestDTO); //
        empleado.setTipoUsuario(tipoUsuario); //
         empleado.setPassword(requestDTO.getPassword()); //


        EmpleadoVentas nuevoEmpleado = empleadoVentasRepository.save(empleado); //
        return empleadoVentasMapper.toResponseDTO(nuevoEmpleado);
    }

    @Transactional
    public EmpleadoVentasResponseDTO actualizarEmpleadoVentas(Long id, EmpleadoVentasRequestDTO requestDTO) {
        EmpleadoVentas empleadoExistente = empleadoVentasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + id)); //

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId())); //

        empleadoVentasMapper.updateEntityFromDto(requestDTO, empleadoExistente); //
        empleadoExistente.setTipoUsuario(tipoUsuario); //
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            empleadoExistente.setPassword(requestDTO.getPassword()); //
        }


        EmpleadoVentas empleadoActualizado = empleadoVentasRepository.save(empleadoExistente); //
        return empleadoVentasMapper.toResponseDTO(empleadoActualizado);
    }

    @Transactional
    public void eliminarEmpleadoVentas(Long id) {
        if (!empleadoVentasRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empleado de Ventas no encontrado con id: " + id); //
        }
        empleadoVentasRepository.deleteById(id); //
    }
}


@Service
class GerenteTiendaService {

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
                .orElseThrow(() -> new ResourceNotFoundException("Gerente de Tienda no encontrado con id: " + id)); //
        return gerenteTiendaMapper.toResponseDTO(gerente);
    }

    @Transactional
    public GerenteTiendaResponseDTO crearGerente(GerenteTiendaRequestDTO requestDTO) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId())); //

        GerenteTienda gerente = gerenteTiendaMapper.toEntity(requestDTO); //
        gerente.setTipoUsuario(tipoUsuario); //
        // gerente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        GerenteTienda nuevoGerente = gerenteTiendaRepository.save(gerente); //
        return gerenteTiendaMapper.toResponseDTO(nuevoGerente);
    }

    @Transactional
    public GerenteTiendaResponseDTO actualizarGerente(Long id, GerenteTiendaRequestDTO requestDTO) {
        GerenteTienda gerenteExistente = gerenteTiendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gerente de Tienda no encontrado con id: " + id)); //

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario no encontrado con id: " + requestDTO.getTipoUsuarioId())); //

        gerenteTiendaMapper.updateEntityFromDto(requestDTO, gerenteExistente); //
        gerenteExistente.setTipoUsuario(tipoUsuario); //

        // if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
        //    gerenteExistente.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // }

        GerenteTienda gerenteActualizado = gerenteTiendaRepository.save(gerenteExistente); //
        return gerenteTiendaMapper.toResponseDTO(gerenteActualizado);
    }

    @Transactional
    public void eliminarGerente(Long id) {
        if (!gerenteTiendaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Gerente de Tienda no encontrado con id: " + id); //
        }
        gerenteTiendaRepository.deleteById(id); //
    }
}


@Service
class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoMapper pedidoMapper;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.pedidoMapper = pedidoMapper;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id)); //
        return pedidoMapper.toResponseDTO(pedido);
    }

    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO requestDTO) {
        Cliente cliente = clienteRepository.findById(requestDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + requestDTO.getClienteId())); //

        Pedido pedido = pedidoMapper.toEntity(requestDTO); //
        pedido.setCliente(cliente); //
        pedido.setFechaPedido(LocalDateTime.now()); //

        Pedido nuevoPedido = pedidoRepository.save(pedido); //
        return pedidoMapper.toResponseDTO(nuevoPedido);
    }

    @Transactional
    public PedidoResponseDTO actualizarPedido(Long id, PedidoRequestDTO requestDTO) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id)); //

        pedidoMapper.updateEntityFromDto(requestDTO, pedidoExistente); //

        if (pedidoExistente.getCliente() != null &&
            requestDTO.getClienteId() != null &&
            !pedidoExistente.getCliente().getId().equals(requestDTO.getClienteId())) {
             Cliente nuevoCliente = clienteRepository.findById(requestDTO.getClienteId())
                 .orElseThrow(() -> new ResourceNotFoundException("Cliente (nuevo) no encontrado con id: " + requestDTO.getClienteId())); //
             pedidoExistente.setCliente(nuevoCliente); //
        }

        Pedido pedidoActualizado = pedidoRepository.save(pedidoExistente); //
        return pedidoMapper.toResponseDTO(pedidoActualizado);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido no encontrado con id: " + id); //
        }
        pedidoRepository.deleteById(id); //
    }
}