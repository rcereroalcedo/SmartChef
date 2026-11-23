package es.safareyes.smartchef.dto.request;

import es.safareyes.smartchef.modelos.Dificultad;
import es.safareyes.smartchef.validaciones.PasosConsecutivos;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record RecetaRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede pasar de 150 caracteres")
        String nombre,

        @Size(max = 2000, message = "La descripción no puede pasar de 2000 caracteres")
        String descripcion,

        @NotNull(message = "Las raciones son obligatorias")
        @Min(value = 1, message = "Mínimo 1 ración")
        @Max(value = 12, message = "Máximo 12 raciones")
        Integer raciones,

        @NotNull(message = "El tiempo de preparación es obligatorio")
        @Positive(message = "El tiempo debe ser mayor que 0")
        Integer tiempo,

        @NotNull(message = "La dificultad es obligatoria")
        Dificultad dificultad,

        @NotEmpty(message = "La receta necesita al menos un ingrediente")
        @Size(max = 30, message = "Una receta no puede tener más de 30 ingredientes")
        @Valid
        List<IngredienteCantidadRequest> ingredientes,

        @NotEmpty(message = "La receta necesita al menos un paso")
        @PasosConsecutivos
        @Valid
        List<PasoRequest> pasos
) { }