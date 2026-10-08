package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Preferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPreferenciaRepository extends JpaRepository<Preferencia, Long> {
}
