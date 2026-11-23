package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.Dificultad;
import es.safareyes.smartchef.modelos.Receta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface IRecetaRepository extends JpaRepository<Receta, Long> {

    @Query("""
            SELECT r FROM Receta r
            WHERE r.activa = TRUE
              AND LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
              AND (:dificultad IS NULL OR r.dificultad = :dificultad)
              AND (:caloriasMax IS NULL OR r.calorias <= :caloriasMax)
              AND (:tiempoMax IS NULL OR r.tiempo <= :tiempoMax)
              AND (:vegetariana = FALSE OR NOT EXISTS (
                    SELECT 1 FROM RecetaIngrediente ri
                    WHERE ri.receta = r AND ri.ingrediente.vegetariano = FALSE))
              AND (:sinGluten = FALSE OR NOT EXISTS (
                    SELECT 1 FROM RecetaIngrediente ri
                    WHERE ri.receta = r AND ri.ingrediente.sinGluten = FALSE))
              AND (:rapida = FALSE OR r.tiempo <= :tiempoRapido)
              AND (:economica = FALSE OR :costeMaxRacion * r.raciones >= (
                    SELECT COALESCE(SUM(x.cantidad * x.ingrediente.precio), 0.0)
                    FROM RecetaIngrediente x WHERE x.receta = r))
            """)
    Page<Receta> buscarCatalogo(@Param("nombre") String nombre,
                                @Param("dificultad") Dificultad dificultad,
                                @Param("caloriasMax") Integer caloriasMax,
                                @Param("tiempoMax") Integer tiempoMax,
                                @Param("vegetariana") boolean vegetariana,
                                @Param("sinGluten") boolean sinGluten,
                                @Param("rapida") boolean rapida,
                                @Param("tiempoRapido") int tiempoRapido,
                                @Param("economica") boolean economica,
                                @Param("costeMaxRacion") BigDecimal costeMaxRacion,
                                Pageable pageable);

    // Detalle: trae la receta con sus ingredientes en una sola consulta (evita N+1)
    @Query("""
            SELECT r FROM Receta r
            LEFT JOIN FETCH r.ingredientes ri
            LEFT JOIN FETCH ri.ingrediente
            WHERE r.id = :id AND r.activa = TRUE
            """)
    Optional<Receta> buscarActivaConIngredientes(@Param("id") Long id);

    Optional<Receta> findByIdAndActivaTrue(Long id);
}