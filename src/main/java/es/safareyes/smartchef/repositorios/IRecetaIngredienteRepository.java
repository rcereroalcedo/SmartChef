package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.RecetaIngrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface IRecetaIngredienteRepository extends JpaRepository<RecetaIngrediente, Long> {
    @Query("""
            SELECT new map(i.id AS id, i.nombre AS nombre, ri.cantidad AS cantidad,
                           i.tipoCantidad AS tipoCantidad, i.vegetariano AS vegetariano,
                           i.sinGluten AS sinGluten, ri.cantidad * i.precio AS coste)
            FROM RecetaIngrediente ri
            JOIN ri.ingrediente i
            WHERE ri.receta.id = :recetaId
            ORDER BY i.nombre
            """)
    List<Map<String, Object>> findIngredientesDeReceta(@Param("recetaId") Long recetaId);

    @Query("""
            SELECT COALESCE(SUM(ri.cantidad * i.precio), 0)
            FROM RecetaIngrediente ri
            JOIN ri.ingrediente i
            WHERE ri.receta.id = :recetaId
            """)
    BigDecimal calcularCoste(@Param("recetaId") Long recetaId);

    long countByRecetaIdAndIngredienteVegetarianoFalse(Long recetaId);

    long countByRecetaIdAndIngredienteSinGlutenFalse(Long recetaId);
}
