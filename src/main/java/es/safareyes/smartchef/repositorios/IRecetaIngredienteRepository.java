package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.RecetaIngrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IRecetaIngredienteRepository extends JpaRepository<RecetaIngrediente, Long> {

    // RN-04: solo recetas de las que se tiene MÁS de la mitad de los ingredientes
    @Query("""
            SELECT ri.receta.id AS recetaId,
                   COUNT(ri) AS total,
                   SUM(CASE WHEN ri.ingrediente.id IN :disponibles THEN 1 ELSE 0 END) AS tengo
            FROM RecetaIngrediente ri
            WHERE ri.receta.activa = TRUE
            GROUP BY ri.receta.id
            HAVING SUM(CASE WHEN ri.ingrediente.id IN :disponibles THEN 1 ELSE 0 END) * 2 > COUNT(ri)
            """)
    List<CoincidenciaView> buscarCoincidencias(@Param("disponibles") Collection<Long> disponibles);

    @Query("""
            SELECT ri FROM RecetaIngrediente ri
            JOIN FETCH ri.ingrediente
            WHERE ri.receta.id IN :recetaIds
            """)
    List<RecetaIngrediente> findConIngredientes(@Param("recetaIds") Collection<Long> recetaIds);
}