package es.safareyes.smartchef.util;

import es.safareyes.smartchef.modelos.Receta;
import es.safareyes.smartchef.modelos.RecetaIngrediente;
import es.safareyes.smartchef.modelos.TipoPreferencia;

import java.math.BigDecimal;
import java.util.List;

/** RN-02: una receta cumple una preferencia según sus ingredientes, su tiempo o su coste. */
public final class ReglasReceta {

    public static final int TIEMPO_RAPIDO_MAX = 30;
    public static final BigDecimal COSTE_ECONOMICO_MAX = new BigDecimal("2.50");

    private ReglasReceta() { }

    public static boolean cumple(TipoPreferencia preferencia, Receta receta, List<RecetaIngrediente> lineas) {
        return switch (preferencia) {
            case VEGETARIANO -> lineas.stream()
                    .allMatch(l -> Boolean.TRUE.equals(l.getIngrediente().getVegetariano()));
            case SIN_GLUTEN -> lineas.stream()
                    .allMatch(l -> Boolean.TRUE.equals(l.getIngrediente().getSinGluten()));
            case RAPIDO -> receta.getTiempo() <= TIEMPO_RAPIDO_MAX;
            case ECONOMICO -> {
                BigDecimal coste = lineas.stream()
                        .map(l -> l.getCantidad().multiply(l.getIngrediente().getPrecio()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                yield coste.compareTo(COSTE_ECONOMICO_MAX.multiply(BigDecimal.valueOf(receta.getRaciones()))) <= 0;
            }
        };
    }
}