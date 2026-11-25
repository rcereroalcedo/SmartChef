package es.safareyes.smartchef.dto.response;

import es.safareyes.smartchef.modelos.EstadoListaCompra;

import java.util.List;

public record ListaCompraResponse(
        Long id,
        EstadoListaCompra estado,
        List<LineaListaCompraResponse> lineas
) { }