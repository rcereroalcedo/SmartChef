package es.safareyes.smartchef.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record IngredienteCantidadRequest(
        @NotNull(message = "Indica el ingrediente")
        Long ingredienteId,

        @NotNull(message = "Indica la cantidad")
        @Positive(message = "La cantidad debe ser mayor que 0")
        BigDecimal cantidad
) { }