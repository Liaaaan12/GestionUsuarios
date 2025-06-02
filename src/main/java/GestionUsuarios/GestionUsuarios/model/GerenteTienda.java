package GestionUsuarios.GestionUsuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor;

@Entity
@Table(name = "gerentes_tienda")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
// @AllArgsConstructor // Si es necesario
public class GerenteTienda extends Usuario {

    @Column(name = "anos_experiencia")
    private Integer anosExperiencia; //

    @Column(name = "tienda_asignada")
    private String tiendaAsignada; //
}