package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Coleccion;
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
}