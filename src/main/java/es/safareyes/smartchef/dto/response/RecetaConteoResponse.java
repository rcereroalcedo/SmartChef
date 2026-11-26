package es.safareyes.smartchef.dto.response;

public record RecetaConteoResponse(
        Long recetaId,
        String nombre,
        Long total
) { }