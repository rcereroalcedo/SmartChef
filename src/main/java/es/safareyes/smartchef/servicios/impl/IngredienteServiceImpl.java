package es.safareyes.smartchef.servicios.impl;

import es.safareyes.smartchef.dto.response.IngredienteResponse;
import es.safareyes.smartchef.mapper.IngredienteMapper;
import es.safareyes.smartchef.repositorios.IIngredienteRepository;
import es.safareyes.smartchef.servicios.IngredienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredienteServiceImpl implements IngredienteService {

    private final IIngredienteRepository ingredienteRepository;
    private final IngredienteMapper ingredienteMapper;

    @Override
    public Page<IngredienteResponse> buscar(String texto, Pageable pageable) {
        String filtro = texto == null ? "" : texto.trim();
        return ingredienteRepository
                .findByNombreContainingIgnoreCase(filtro, pageable)
                .map(ingredienteMapper::toResponse);
    }
}