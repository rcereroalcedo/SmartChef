package es.safareyes.smartchef.dto.request;

import es.safareyes.smartchef.modelos.TipoPreferencia;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BuscarPorIngredientesRequest(
        @Size(max = 100, message = "Máximo 100 ingredientes")
        List<Long> ingredienteIds,

        List<TipoPreferencia> preferencias
) { }