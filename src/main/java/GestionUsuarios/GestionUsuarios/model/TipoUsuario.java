package GestionUsuarios.GestionUsuarios.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tipos_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    // Opcional: Si quieres navegar desde TipoUsuario a Usuarios
    // @OneToMany(mappedBy = "tipoUsuario")
    // private List<Usuario> usuarios;
}