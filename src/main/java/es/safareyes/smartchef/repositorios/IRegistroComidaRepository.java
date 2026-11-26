package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.MomentoComida;
import es.safareyes.smartchef.modelos.RegistroComida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IRegistroComidaRepository extends JpaRepository<RegistroComida, Long> {

    boolean existsByUsuarioIdAndFechaAndMomento(Long usuarioId, LocalDate fecha, MomentoComida momento);

    @Query("""
            SELECT r FROM RegistroComida r
            LEFT JOIN FETCH r.receta
            WHERE r.usuario.id = :usuarioId
              AND r.fecha BETWEEN :desde AND :hasta
            ORDER BY r.fecha ASC
            """)
    List<RegistroComida> findSemana(@Param("usuarioId") Long usuarioId,
                                    @Param("desde") LocalDate desde,
                                    @Param("hasta") LocalDate hasta);
}