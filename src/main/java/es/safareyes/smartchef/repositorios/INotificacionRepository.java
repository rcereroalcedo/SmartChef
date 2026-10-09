package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INotificacionRepository extends JpaRepository<Notificacion, Integer> {
}