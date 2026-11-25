package es.safareyes.smartchef.servicios;

import es.safareyes.smartchef.dto.response.ColeccionResponse;

import java.util.List;

public interface ColeccionService {

    List<ColeccionResponse> listar(Long usuarioId);

    ColeccionResponse guardarReceta(Long usuarioId, String nombreColeccion, Long recetaId);
}