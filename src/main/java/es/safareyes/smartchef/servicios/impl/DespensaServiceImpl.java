package es.safareyes.smartchef.servicios.impl;

import es.safareyes.smartchef.dto.request.DespensaRequest;
import es.safareyes.smartchef.dto.response.DespensaResponse;
import es.safareyes.smartchef.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchef.mapper.DespensaMapper;
import es.safareyes.smartchef.modelos.Despensa;
import es.safareyes.smartchef.modelos.Ingrediente;
import es.safareyes.smartchef.modelos.Usuario;
import es.safareyes.smartchef.repositorios.IDespensaRepository;
import es.safareyes.smartchef.repositorios.IIngredienteRepository;
import es.safareyes.smartchef.repositorios.IUsuarioRepository;
import es.safareyes.smartchef.servicios.DespensaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DespensaServiceImpl implements DespensaService {

    private final IDespensaRepository despensaRepository;
    private final IIngredienteRepository ingredienteRepository;
    private final IUsuarioRepository usuarioRepository;
    private final DespensaMapper despensaMapper;

    @Override
    public List<DespensaResponse> listar(Long usuarioId) {
        buscarUsuario(usuarioId);
        return despensaMapper.toResponseList(despensaRepository.findDespensaOrdenada(usuarioId));
    }

    @Override
    @Transactional
    public Optional<DespensaResponse> guardar(Long usuarioId, Long ingredienteId, DespensaRequest request) {
        Usuario usuario = buscarUsuario(usuarioId);
        Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el ingrediente con id " + ingredienteId));

        Optional<Despensa> existente = despensaRepository
                .findByUsuarioIdAndIngredienteId(usuarioId, ingredienteId);

        // Cantidad 0 = el ingrediente sale de la despensa
        if (request.cantidad().signum() == 0) {
            existente.ifPresent(despensaRepository::delete);
            log.info("Usuario {} elimina el ingrediente {} de su despensa", usuarioId, ingredienteId);
            return Optional.empty();
        }

        Despensa despensa = existente.orElseGet(() -> {
            Despensa nueva = new Despensa();
            nueva.setUsuario(usuario);
            nueva.setIngrediente(ingrediente);
            return nueva;
        });
        despensaMapper.actualizar(request, despensa);

        Despensa guardada = despensaRepository.save(despensa);
        log.info("Usuario {} guarda {} del ingrediente {} en su despensa",
                usuarioId, request.cantidad(), ingredienteId);
        return Optional.of(despensaMapper.toResponse(guardada));
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + id));
    }
}