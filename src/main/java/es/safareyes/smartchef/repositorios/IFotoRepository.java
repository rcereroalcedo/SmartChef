package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFotoRepository extends JpaRepository<Foto, Long> {
}
