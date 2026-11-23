package es.safareyes.smartchef.servicios;

import es.safareyes.smartchef.dto.request.FiltroRecetas;
import es.safareyes.smartchef.dto.request.RecetaActualizarRequest;
import es.safareyes.smartchef.dto.request.RecetaRequest;
import es.safareyes.smartchef.dto.response.RecetaDetalleResponse;
import es.safareyes.smartchef.dto.response.RecetaResumenResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecetaService {

    Page<RecetaResumenResponse> listar(FiltroRecetas filtro, Pageable pageable);

    RecetaDetalleResponse obtener(Long id);

    RecetaDetalleResponse crear(RecetaRequest request, Long autorId);

    RecetaDetalleResponse actualizar(Long id, RecetaActualizarRequest request, Long usuarioId);

    void retirar(Long id, Long usuarioId);
}