package es.safareyes.smartchef.dto.response;

import java.util.List;

public record ColeccionResponse(
        Long id,
        String nombre,
        List<RecetaEnColeccionResponse> recetas
) { }