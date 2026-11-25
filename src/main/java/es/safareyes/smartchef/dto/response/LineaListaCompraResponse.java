package es.safareyes.smartchef.dto.response;

import es.safareyes.smartchef.modelos.TipoCantidad;

import java.math.BigDecimal;

public record LineaListaCompraResponse(
        Long id,
        Long ingredienteId,
        String nombre,
        TipoCantidad unidad,
        BigDecimal cantidad,
        boolean comprado
) { }