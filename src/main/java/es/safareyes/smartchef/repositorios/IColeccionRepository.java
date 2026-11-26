package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.dto.response.RecetaConteoResponse;
import es.safareyes.smartchef.modelos.Coleccion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {

    @Query("""
            SELECT c FROM Coleccion c
            LEFT JOIN FETCH c.recetas
            WHERE c.usuario.id = :usuarioId
            ORDER BY c.nombre ASC
            """)
    List<Coleccion> findConRecetas(@Param("usuarioId") Long usuarioId);

    Optional<Coleccion> findByUsuarioIdAndNombreIgnoreCase(Long usuarioId, String nombre);

    boolean existsByUsuarioIdAndNombreIgnoreCase(Long usuarioId, String nombre);

    // recetas más guardadas en colecciones
    @Query("""
            SELECT new es.safareyes.smartchef.dto.response.RecetaConteoResponse(r.id, r.nombre, COUNT(c))
            FROM Coleccion c
            JOIN c.recetas r
            WHERE r.activa = TRUE
            GROUP BY r.id, r.nombre
            ORDER BY COUNT(c) DESC, r.nombre ASC
            """)
    List<RecetaConteoResponse> findMasGuardadas(Pageable pageable);
}