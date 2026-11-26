package es.safareyes.smartchef.dto.response;

import es.safareyes.smartchef.modelos.MomentoComida;

import java.time.LocalDate;

public record RegistroComidaResponse(
        Long id,
        LocalDate fecha,
        MomentoComida momento,
        Long recetaId,
        String recetaNombre,
        String descripcion,
        Integer calorias
) { }