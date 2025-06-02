package GestionUsuarios.GestionUsuarios.repository;

import GestionUsuarios.GestionUsuarios.model.GerenteTienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GerenteTiendaRepository extends JpaRepository<GerenteTienda, Long> {
}