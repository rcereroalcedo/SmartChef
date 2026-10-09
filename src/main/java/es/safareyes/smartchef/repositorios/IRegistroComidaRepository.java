package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.RegistroComida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegistroComidaRepository extends JpaRepository<RegistroComida, Long> {
}
