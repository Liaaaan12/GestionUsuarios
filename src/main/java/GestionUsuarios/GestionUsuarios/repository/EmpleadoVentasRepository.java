package GestionUsuarios.GestionUsuarios.repository;

import GestionUsuarios.GestionUsuarios.model.EmpleadoVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoVentasRepository extends JpaRepository<EmpleadoVentas, Long> {
}