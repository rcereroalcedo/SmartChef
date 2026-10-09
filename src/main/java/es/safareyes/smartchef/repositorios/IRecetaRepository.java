package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Dificultad;
import es.safareyes.smartchef.modelos.Receta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface IRecetaRepository extends JpaRepository<Receta, Integer> {
    @Query(value = """
            SELECT new map(r.id AS id, r.nombre AS nombre, r.raciones AS raciones,
                           r.tiempo AS tiempo, r.calorias AS calorias, r.dificultad AS dificultad,
                           (SELECT MIN(f.url) FROM Foto f
                            WHERE f.receta = r AND f.esPortada = TRUE) AS fotoPortada)
            FROM Receta r
            WHERE LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
              AND (:dificultad IS NULL OR r.dificultad = :dificultad)
              AND (:caloriasMax IS NULL OR r.calorias <= :caloriasMax)
              AND (:tiempoMax IS NULL OR r.tiempo <= :tiempoMax)
              AND (:vegetariana = FALSE OR NOT EXISTS (
                    SELECT 1 FROM RecetaIngrediente ri
                    WHERE ri.receta = r AND ri.ingrediente.vegetariano = FALSE))
              AND (:sinGluten = FALSE OR NOT EXISTS (
                    SELECT 1 FROM RecetaIngrediente ri
                    WHERE ri.receta = r AND ri.ingrediente.sinGluten = FALSE))
            ORDER BY r.nombre
            """,
            countQuery = """
            SELECT COUNT(r) FROM Receta r
            WHERE LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
              AND (:dificultad IS NULL OR r.dificultad = :dificultad)
              AND (:caloriasMax IS NULL OR r.calorias <= :caloriasMax)
              AND (:tiempoMax IS NULL OR r.tiempo <= :tiempoMax)
              AND (:vegetariana = FALSE OR NOT EXISTS (
                    SELECT 1 FROM RecetaIngrediente ri
                    WHERE ri.receta = r AND ri.ingrediente.vegetariano = FALSE))
              AND (:sinGluten = FALSE OR NOT EXISTS (
                    SELECT 1 FROM RecetaIngrediente ri
                    WHERE ri.receta = r AND ri.ingrediente.sinGluten = FALSE))
            """)
    Page<Map<String, Object>> buscarCatalogo(@Param("nombre") String nombre,
                                             @Param("dificultad") Dificultad dificultad,
                                             @Param("caloriasMax") Integer caloriasMax,
                                             @Param("tiempoMax") Integer tiempoMax,
                                             @Param("vegetariana") boolean vegetariana,
                                             @Param("sinGluten") boolean sinGluten,
                                             Pageable pageable);

    @Query("""
            SELECT new map(r.id AS id, r.nombre AS nombre, r.raciones AS raciones,
                           r.tiempo AS tiempo, r.calorias AS calorias,
                           r.dificultad AS dificultad, r.descripcion AS descripcion)
            FROM Receta r
            WHERE r.id = :id
            """)
    Optional<Map<String, Object>> findCabecera(@Param("id") Long id);
}
