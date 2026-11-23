package es.safareyes.smartchef.mapper;

import java.math.BigDecimal;

public record CalculoReceta(
        BigDecimal costeTotal,
        BigDecimal costePorRacion,
        boolean vegetariana,
        boolean sinGluten,
        boolean rapida,
        boolean economica
) { }