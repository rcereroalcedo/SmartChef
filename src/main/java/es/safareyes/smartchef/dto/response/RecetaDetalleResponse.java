package es.safareyes.smartchef.dto.response;

import es.safareyes.smartchef.modelos.Dificultad;

import java.math.BigDecimal;
import java.util.List;

public record RecetaDetalleResponse(
        Long id,
        String nombre,
        String descripcion,
        Integer raciones,
        Integer tiempo,
        Dificultad dificultad,
        String autor,
        Integer calorias,
        BigDecimal costeTotal,
        BigDecimal costePorRacion,
        boolean vegetariana,
        boolean sinGluten,
        boolean rapida,
        boolean economica,
        List<IngredienteRecetaResponse> ingredientes,
        List<PasoResponse> pasos,
        List<FotoResponse> fotos
) { }