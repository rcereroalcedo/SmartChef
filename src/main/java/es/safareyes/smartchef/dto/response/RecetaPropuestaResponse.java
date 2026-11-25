package es.safareyes.smartchef.dto.response;

import es.safareyes.smartchef.modelos.Dificultad;

import java.util.List;

public record RecetaPropuestaResponse(
        Long id,
        String nombre,
        Integer raciones,
        Integer tiempo,
        Integer calorias,
        Dificultad dificultad,
        String fotoPortada,
        int porcentajeCoincidencia,
        int ingredientesDisponibles,
        int ingredientesTotales,
        List<IngredienteFaltanteResponse> faltan
) { }