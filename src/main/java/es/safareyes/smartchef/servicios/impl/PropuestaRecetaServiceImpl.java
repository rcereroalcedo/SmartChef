package es.safareyes.smartchef.servicios.impl;

import es.safareyes.smartchef.dto.request.BuscarPorIngredientesRequest;
import es.safareyes.smartchef.dto.response.RecetaPropuestaResponse;
import es.safareyes.smartchef.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchef.mapper.Coincidencia;
import es.safareyes.smartchef.mapper.PropuestaMapper;
import es.safareyes.smartchef.modelos.Preferencia;
import es.safareyes.smartchef.modelos.Receta;
import es.safareyes.smartchef.modelos.RecetaIngrediente;
import es.safareyes.smartchef.modelos.TipoPreferencia;
import es.safareyes.smartchef.modelos.Usuario;
import es.safareyes.smartchef.repositorios.CoincidenciaView;
import es.safareyes.smartchef.repositorios.IDespensaRepository;
import es.safareyes.smartchef.repositorios.IRecetaIngredienteRepository;
import es.safareyes.smartchef.repositorios.IRecetaRepository;
import es.safareyes.smartchef.repositorios.IUsuarioRepository;
import es.safareyes.smartchef.servicios.PropuestaRecetaService;
import es.safareyes.smartchef.util.Fechas;
import es.safareyes.smartchef.util.ReglasReceta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PropuestaRecetaServiceImpl implements PropuestaRecetaService {

    private final IRecetaRepository recetaRepository;
    private final IRecetaIngredienteRepository recetaIngredienteRepository;
    private final IDespensaRepository despensaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final PropuestaMapper propuestaMapper;

    @Override
    public List<RecetaPropuestaResponse> buscarPorIngredientes(Long usuarioId, BuscarPorIngredientesRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId));

        // 1. Ingredientes con los que se cuenta: los indicados o, si no, la despensa sin caducados (RN-04)
        Set<Long> disponibles = ingredientesDisponibles(usuarioId, request);
        if (disponibles.isEmpty()) {
            return List.of();
        }

        // 2. Preferencias: las de la petición o, si no vienen, las del perfil (RN-03)
        Set<TipoPreferencia> preferencias = preferenciasAplicables(usuario, request);

        // 3. Recetas de las que se tiene más de la mitad de los ingredientes
        List<CoincidenciaView> coincidencias = recetaIngredienteRepository.buscarCoincidencias(disponibles);
        if (coincidencias.isEmpty()) {
            return List.of();
        }
        List<Long> ids = coincidencias.stream().map(CoincidenciaView::getRecetaId).toList();

        Map<Long, Receta> recetas = recetaRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Receta::getId, Function.identity()));
        Map<Long, List<RecetaIngrediente>> lineasPorReceta = recetaIngredienteRepository
                .findConIngredientes(ids).stream()
                .collect(Collectors.groupingBy(ri -> ri.getReceta().getId()));

        // 4. Armar cada propuesta indicando lo que falta
        List<RecetaPropuestaResponse> propuestas = new ArrayList<>();
        for (Long id : ids) {
            Receta receta = recetas.get(id);
            List<RecetaIngrediente> lineas = lineasPorReceta.getOrDefault(id, List.of());
            if (receta == null || lineas.isEmpty()) continue;

            boolean cumple = preferencias.stream().allMatch(p -> ReglasReceta.cumple(p, receta, lineas));
            if (!cumple) continue;

            List<RecetaIngrediente> faltan = lineas.stream()
                    .filter(l -> !disponibles.contains(l.getIngrediente().getId()))
                    .toList();
            int total = lineas.size();
            int tengo = total - faltan.size();
            int porcentaje = Math.round(tengo * 100f / total);

            propuestas.add(propuestaMapper.toPropuesta(receta, new Coincidencia(porcentaje, tengo, total, faltan)));
        }

        // 5. Primero las que más ingredientes tienen
        propuestas.sort(Comparator.comparingInt(RecetaPropuestaResponse::porcentajeCoincidencia).reversed()
                .thenComparingInt(r -> r.faltan().size())
                .thenComparing(RecetaPropuestaResponse::nombre));

        log.info("Usuario {}: {} recetas propuestas con {} ingredientes disponibles",
                usuarioId, propuestas.size(), disponibles.size());
        return propuestas;
    }

    private Set<Long> ingredientesDisponibles(Long usuarioId, BuscarPorIngredientesRequest request) {
        if (request != null && request.ingredienteIds() != null && !request.ingredienteIds().isEmpty()) {
            return new HashSet<>(request.ingredienteIds());
        }
        return despensaRepository.findNoCaducados(usuarioId, Fechas.hoy()).stream()
                .map(d -> d.getIngrediente().getId())
                .collect(Collectors.toSet());
    }

    private Set<TipoPreferencia> preferenciasAplicables(Usuario usuario, BuscarPorIngredientesRequest request) {
        if (request != null && request.preferencias() != null) {
            return new HashSet<>(request.preferencias());
        }
        return usuario.getPreferencias().stream()
                .map(Preferencia::getTipo)
                .collect(Collectors.toSet());
    }
}