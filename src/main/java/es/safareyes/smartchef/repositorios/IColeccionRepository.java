package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {
}
