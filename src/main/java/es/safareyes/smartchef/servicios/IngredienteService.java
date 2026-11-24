package es.safareyes.smartchef.servicios;

import es.safareyes.smartchef.dto.response.IngredienteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IngredienteService {

    Page<IngredienteResponse> buscar(String texto, Pageable pageable);
}