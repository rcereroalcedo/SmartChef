package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Paso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPasoRepository extends JpaRepository<Paso, Long> {
}
