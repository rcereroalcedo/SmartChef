package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IFotoRepository extends JpaRepository<Foto, Long> {
    @Query("""
            SELECT new map(f.url AS url, f.esPortada AS esPortada)
            FROM Foto f
            WHERE f.receta.id = :recetaId
            ORDER BY f.esPortada DESC, f.id
            """)
    List<Map<String, Object>> findFotosDeReceta(@Param("recetaId") Long recetaId);
}
