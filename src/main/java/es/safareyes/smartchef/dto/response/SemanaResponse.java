package es.safareyes.smartchef.dto.response;

import java.time.LocalDate;
import java.util.List;

public record SemanaResponse(
        LocalDate desde,
        LocalDate hasta,
        int caloriasSemana,
        List<DiaSemanaResponse> dias
) { }