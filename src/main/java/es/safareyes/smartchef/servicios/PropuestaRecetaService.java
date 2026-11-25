package es.safareyes.smartchef.servicios;

import es.safareyes.smartchef.dto.request.BuscarPorIngredientesRequest;
import es.safareyes.smartchef.dto.response.RecetaPropuestaResponse;

import java.util.List;

public interface PropuestaRecetaService {

    List<RecetaPropuestaResponse> buscarPorIngredientes(Long usuarioId, BuscarPorIngredientesRequest request);
}