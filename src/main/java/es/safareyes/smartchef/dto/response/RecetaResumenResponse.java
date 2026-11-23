package es.safareyes.smartchef.dto.response;

import es.safareyes.smartchef.modelos.Dificultad;

public record RecetaResumenResponse(
        Long id,
        String nombre,
        Integer raciones,
        Integer tiempo,
        Integer calorias,
        Dificultad dificultad,
        String fotoPortada,
        String autor
) { }