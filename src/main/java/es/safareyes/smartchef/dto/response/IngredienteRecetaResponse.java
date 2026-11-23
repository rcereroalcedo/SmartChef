package es.safareyes.smartchef.dto.response;

import es.safareyes.smartchef.modelos.TipoCantidad;

import java.math.BigDecimal;

public record IngredienteRecetaResponse(
        Long ingredienteId,
        String nombre,
        BigDecimal cantidad,
        TipoCantidad unidad,
        BigDecimal coste,
        boolean vegetariano,
        boolean sinGluten
) { }