package es.safareyes.smartchef.servicios;

import es.safareyes.smartchef.dto.request.DespensaRequest;
import es.safareyes.smartchef.dto.response.DespensaResponse;

import java.util.List;
import java.util.Optional;

public interface DespensaService {

    List<DespensaResponse> listar(Long usuarioId);

    /** Devuelve vacío cuando la cantidad es 0 y el ingrediente se elimina de la despensa. */
    Optional<DespensaResponse> guardar(Long usuarioId, Long ingredienteId, DespensaRequest request);
}