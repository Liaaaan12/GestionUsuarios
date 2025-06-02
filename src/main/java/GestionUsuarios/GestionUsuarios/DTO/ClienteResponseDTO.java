package GestionUsuarios.GestionUsuarios.DTO;

public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    // Agrega otros campos según tu modelo

    public ClienteResponseDTO() {
    }

    public ClienteResponseDTO(Long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFechaNacimiento'");
    }

    public void setRut(String rut) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setRut'");
    }

    public void setTipoUsuario(TipoUsuarioResponseDTO responseDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setTipoUsuario'");
    }

    public void setDireccionEnvio(String direccionEnvio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDireccionEnvio'");
    }
}