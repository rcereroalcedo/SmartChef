package es.safareyes.smartchef.dto.request;

import es.safareyes.smartchef.modelos.Dificultad;
import es.safareyes.smartchef.validaciones.PasosConsecutivos;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record RecetaActualizarRequest(
        @Size(min = 1, max = 150, message = "El nombre debe tener entre 1 y 150 caracteres")
        String nombre,

        @Size(max = 2000, message = "La descripción no puede pasar de 2000 caracteres")
        String descripcion,

        @Min(value = 1, message = "Mínimo 1 ración")
        @Max(value = 12, message = "Máximo 12 raciones")
        Integer raciones,

        @Positive(message = "El tiempo debe ser mayor que 0")
        Integer tiempo,

        Dificultad dificultad,

        @Size(min = 1, max = 30, message = "La receta debe tener entre 1 y 30 ingredientes")
        @Valid
        List<IngredienteCantidadRequest> ingredientes,

        @Size(min = 1, message = "La receta necesita al menos un paso")
        @PasosConsecutivos
        @Valid
        List<PasoRequest> pasos
) { }