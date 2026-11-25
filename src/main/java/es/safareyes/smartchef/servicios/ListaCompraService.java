package es.safareyes.smartchef.servicios;

import es.safareyes.smartchef.dto.request.CerrarListaRequest;
import es.safareyes.smartchef.dto.request.DesdeRecetaRequest;
import es.safareyes.smartchef.dto.response.ListaCompraResponse;

public interface ListaCompraService {

    ListaCompraResponse desdeReceta(Long usuarioId, DesdeRecetaRequest request);

    ListaCompraResponse cerrar(Long listaId, Long usuarioId, CerrarListaRequest request);
}