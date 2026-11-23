package es.safareyes.smartchef.dto.response;

import es.safareyes.smartchef.modelos.TipoCantidad;

import java.math.BigDecimal;

public record IngredienteResponse(
        Long id,
        String nombre,
        boolean vegetariano,
        boolean sinGluten,
        TipoCantidad tipoCantidad,
        BigDecimal precio,
        BigDecimal calorias
) { }