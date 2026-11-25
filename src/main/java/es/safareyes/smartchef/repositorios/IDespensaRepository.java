package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Despensa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IDespensaRepository extends JpaRepository<Despensa, Long> {

    @Query("""
            SELECT d FROM Despensa d
            JOIN FETCH d.ingrediente i
            WHERE d.usuario.id = :usuarioId
            ORDER BY d.caducidad ASC NULLS LAST, i.nombre ASC
            """)
    List<Despensa> findDespensaOrdenada(@Param("usuarioId") Long usuarioId);

    Optional<Despensa> findByUsuarioIdAndIngredienteId(Long usuarioId, Long ingredienteId);

    @Query("""
            SELECT d FROM Despensa d
            JOIN FETCH d.ingrediente
            WHERE d.usuario.id = :usuarioId
              AND (d.caducidad IS NULL OR d.caducidad >= :hoy)
            """)
    List<Despensa> findNoCaducados(@Param("usuarioId") Long usuarioId, @Param("hoy") LocalDate hoy);
}