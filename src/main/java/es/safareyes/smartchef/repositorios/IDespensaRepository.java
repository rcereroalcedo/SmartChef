package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Despensa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IDespensaRepository extends JpaRepository<Despensa, Long> {

    @Query("""
            SELECT new map(d.id AS id, i.id AS ingredienteId, i.nombre AS nombre,
                           i.tipoCantidad AS tipoCantidad, d.cantidad AS cantidad,
                           d.caducidad AS caducidad)
            FROM Despensa d
            JOIN d.ingrediente i
            WHERE d.usuario.id = :usuarioId
            ORDER BY d.caducidad ASC NULLS LAST, i.nombre ASC
            """)
    List<Map<String, Object>> findDespensaOrdenada(@Param("usuarioId") Long usuarioId);
}
