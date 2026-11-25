package es.safareyes.smartchef.dto.request;

import java.util.List;

public record CerrarListaRequest(
        List<Long> lineasCompradas
) { }