package es.safareyes.smartchef.dto.request;

import es.safareyes.smartchef.modelos.Dificultad;

public record FiltroRecetas(
        String nombre,
        Dificultad dificultad,
        Integer caloriasMax,
        Integer tiempoMax,
        boolean vegetariana,
        boolean sinGluten,
        boolean rapida,
        boolean economica
) { }