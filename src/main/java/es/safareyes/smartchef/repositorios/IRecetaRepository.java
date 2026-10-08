package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRecetaRepository extends JpaRepository<Receta, Long> {
}
