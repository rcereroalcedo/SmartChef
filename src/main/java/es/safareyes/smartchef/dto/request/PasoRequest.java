package es.safareyes.smartchef.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PasoRequest(
        @NotNull(message = "El número del paso es obligatorio")
        @Positive(message = "El número del paso debe ser mayor que 0")
        Integer numero,

        @NotBlank(message = "La descripción del paso es obligatoria")
        @Size(max = 1000, message = "La descripción del paso no puede pasar de 1000 caracteres")
        String descripcion
) { }