package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Paso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IPasoRepository extends JpaRepository<Paso, Integer> {
    @Query("""
            SELECT new map(p.numero AS numero, p.descripcion AS descripcion)
            FROM Paso p
            WHERE p.receta.id = :recetaId
            ORDER BY p.numero
            """)
    List<Map<String, Object>> findPasosDeReceta(@Param("recetaId") Long recetaId);
}
