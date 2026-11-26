package es.safareyes.smartchef.dto.response;

import java.time.LocalDate;
import java.util.List;

public record InformeRecetasPopularesResponse(
        LocalDate desde,
        LocalDate hasta,
        List<RecetaConteoResponse> masCocinadas,
        List<RecetaConteoResponse> masGuardadas
) { }