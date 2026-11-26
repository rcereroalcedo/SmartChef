package es.safareyes.smartchef.dto.response;

import java.time.LocalDate;
import java.util.List;

public record DiaSemanaResponse(
        LocalDate fecha,
        int calorias,
        List<RegistroComidaResponse> registros
) { }