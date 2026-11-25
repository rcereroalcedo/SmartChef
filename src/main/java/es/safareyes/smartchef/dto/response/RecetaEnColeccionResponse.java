package es.safareyes.smartchef.dto.response;

import es.safareyes.smartchef.modelos.Dificultad;

public record RecetaEnColeccionResponse(
        Long id,
        String nombre,
        Integer tiempo,
        Dificultad dificultad,
        String fotoPortada,
        boolean retirada
) { }