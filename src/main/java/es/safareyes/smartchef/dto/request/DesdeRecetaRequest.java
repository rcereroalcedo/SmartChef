package es.safareyes.smartchef.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DesdeRecetaRequest(
        @NotNull(message = "Indica la receta")
        Long recetaId,

        @NotNull(message = "Indica para cuántas raciones")
        @Min(value = 1, message = "Mínimo 1 ración")
        @Max(value = 50, message = "Máximo 50 raciones")
        Integer raciones
) { }