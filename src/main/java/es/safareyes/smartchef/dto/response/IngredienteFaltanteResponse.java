package es.safareyes.smartchef.dto.response;

import es.safareyes.smartchef.modelos.TipoCantidad;

import java.math.BigDecimal;

public record IngredienteFaltanteResponse(
        Long ingredienteId,
        String nombre,
        BigDecimal cantidad,
        TipoCantidad unidad
) { }