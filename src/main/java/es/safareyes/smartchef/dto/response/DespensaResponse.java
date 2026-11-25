package es.safareyes.smartchef.dto.response;

import es.safareyes.smartchef.modelos.TipoCantidad;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespensaResponse(
        Long ingredienteId,
        String nombre,
        TipoCantidad unidad,
        BigDecimal cantidad,
        LocalDate caducidad,
        Long diasParaCaducar      // negativo si ya caducó; null si no tiene fecha
) { }