package es.safareyes.smartchef.mapper;

import es.safareyes.smartchef.modelos.RecetaIngrediente;

import java.util.List;

/** Datos calculados por el servicio al buscar por ingredientes (RN-04). */
public record Coincidencia(
        int porcentaje,
        int disponibles,
        int total,
        List<RecetaIngrediente> faltan
) { }