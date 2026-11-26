package es.safareyes.smartchef.servicios.impl;

import es.safareyes.smartchef.dto.request.FiltroRecetas;
import es.safareyes.smartchef.dto.request.IngredienteCantidadRequest;
import es.safareyes.smartchef.dto.request.PasoRequest;
import es.safareyes.smartchef.dto.request.RecetaActualizarRequest;
import es.safareyes.smartchef.dto.request.RecetaRequest;
import es.safareyes.smartchef.dto.response.RecetaDetalleResponse;
import es.safareyes.smartchef.dto.response.RecetaResumenResponse;
import es.safareyes.smartchef.excepciones.AccesoDenegadoException;
import es.safareyes.smartchef.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchef.excepciones.ReglaNegocioException;
import es.safareyes.smartchef.mapper.CalculoReceta;
import es.safareyes.smartchef.mapper.PasoMapper;
import es.safareyes.smartchef.mapper.RecetaMapper;
import es.safareyes.smartchef.modelos.Ingrediente;
import es.safareyes.smartchef.modelos.Paso;
import es.safareyes.smartchef.modelos.Receta;
import es.safareyes.smartchef.modelos.RecetaIngrediente;
import es.safareyes.smartchef.modelos.Rol;
import es.safareyes.smartchef.modelos.Usuario;
import es.safareyes.smartchef.repositorios.IIngredienteRepository;
import es.safareyes.smartchef.repositorios.IRecetaRepository;
import es.safareyes.smartchef.repositorios.IUsuarioRepository;
import es.safareyes.smartchef.servicios.RecetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class RecetaServiceImpl implements RecetaService {

    // RN-02
    private static final int TIEMPO_RAPIDO_MAX = 30;
    private static final BigDecimal COSTE_ECONOMICO_MAX = new BigDecimal("2.50");

    private final IRecetaRepository recetaRepository;
    private final IIngredienteRepository ingredienteRepository;
    private final IUsuarioRepository usuarioRepository;
    private final RecetaMapper recetaMapper;
    private final PasoMapper pasoMapper;

    // ---------- Lecturas ----------

    @Override
    public Page<RecetaResumenResponse> listar(FiltroRecetas filtro, Pageable pageable) {
        String nombre = filtro.nombre() == null ? "" : filtro.nombre();
        return recetaRepository.buscarCatalogo(
                        nombre, filtro.dificultad(), filtro.caloriasMax(), filtro.tiempoMax(),
                        filtro.vegetariana(), filtro.sinGluten(),
                        filtro.rapida(), TIEMPO_RAPIDO_MAX,
                        filtro.economica(), COSTE_ECONOMICO_MAX,
                        pageable)
                .map(recetaMapper::toResumen);
    }

    @Override
    public RecetaDetalleResponse obtener(Long id) {
        Receta receta = recetaRepository.buscarActivaConIngredientes(id)
                .orElseThrow(() -> noEncontrada(id));
        return construirDetalle(receta);
    }

    // ---------- Escrituras ----------

    @Override
    @Transactional
    public RecetaDetalleResponse crear(RecetaRequest request, Long autorId) {
        Usuario autor = buscarUsuario(autorId);

        Receta receta = recetaMapper.toEntity(request);
        receta.setAutor(autor);
        sustituirIngredientes(receta, request.ingredientes());
        receta.setCalorias(calcularCaloriasPorRacion(receta));

        Receta guardada = recetaRepository.save(receta);
        log.info("Receta {} creada por el usuario {}", guardada.getId(), autorId);
        return construirDetalle(guardada);
    }

    @Override
    @Transactional
    public RecetaDetalleResponse actualizar(Long id, RecetaActualizarRequest request, Long usuarioId) {
        Receta receta = recetaRepository.findByIdAndActivaTrue(id)
                .orElseThrow(() -> noEncontrada(id));
        comprobarPermiso(receta, usuarioId);

        recetaMapper.actualizar(request, receta);            // campos simples, solo los no nulos
        if (request.ingredientes() != null) {
            sustituirIngredientes(receta, request.ingredientes());
        }
        if (request.pasos() != null) {
            sustituirPasos(receta, request.pasos());
        }
        receta.setCalorias(calcularCaloriasPorRacion(receta));

        log.info("Receta {} actualizada por el usuario {}", id, usuarioId);
        return construirDetalle(receta);
    }

    @Override
    @Transactional
    public void retirar(Long id, Long usuarioId) {
        Receta receta = recetaRepository.findByIdAndActivaTrue(id)
                .orElseThrow(() -> noEncontrada(id));
        comprobarPermiso(receta, usuarioId);

        receta.setActiva(false);                             // baja lógica
        log.info("Receta {} retirada por el usuario {}", id, usuarioId);
    }

    // ---------- Reglas de negocio y apoyo ----------

    private void comprobarPermiso(Receta receta, Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        boolean esAutor = receta.getAutor().getId().equals(usuarioId);
        if (!esAutor && usuario.getRol() != Rol.ADMIN) {
            throw new AccesoDenegadoException("Solo el autor de la receta o un administrador puede modificarla");
        }
    }

    private void sustituirIngredientes(Receta receta, List<IngredienteCantidadRequest> lineas) {
        Set<Long> ids = new HashSet<>();
        for (IngredienteCantidadRequest linea : lineas) {
            if (!ids.add(linea.ingredienteId())) {
                throw new ReglaNegocioException(
                        "El ingrediente con id " + linea.ingredienteId() + " está repetido en la receta");
            }
        }

        Map<Long, Ingrediente> catalogo = ingredienteRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Ingrediente::getId, Function.identity()));

        receta.getIngredientes().clear();
        for (IngredienteCantidadRequest linea : lineas) {
            Ingrediente ingrediente = catalogo.get(linea.ingredienteId());
            if (ingrediente == null) {
                throw new RecursoNoEncontradoException(
                        "No existe el ingrediente con id " + linea.ingredienteId());
            }
            RecetaIngrediente nueva = new RecetaIngrediente();
            nueva.setReceta(receta);
            nueva.setIngrediente(ingrediente);
            nueva.setCantidad(linea.cantidad());
            receta.getIngredientes().add(nueva);
        }
    }

    private void sustituirPasos(Receta receta, List<PasoRequest> pasos) {
        receta.getPasos().clear();
        for (PasoRequest request : pasos) {
            Paso paso = pasoMapper.toEntity(request);
            paso.setReceta(receta);
            receta.getPasos().add(paso);
        }
    }

    private int calcularCaloriasPorRacion(Receta receta) {
        BigDecimal total = receta.getIngredientes().stream()
                .map(ri -> ri.getCantidad().multiply(ri.getIngrediente().getCalorias()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(receta.getRaciones()), 0, RoundingMode.HALF_UP).intValue();
    }

    private CalculoReceta calcular(Receta receta) {
        BigDecimal coste = receta.getIngredientes().stream()
                .map(ri -> ri.getCantidad().multiply(ri.getIngrediente().getPrecio()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal raciones = BigDecimal.valueOf(receta.getRaciones());

        boolean vegetariana = receta.getIngredientes().stream()
                .allMatch(ri -> Boolean.TRUE.equals(ri.getIngrediente().getVegetariano()));
        boolean sinGluten = receta.getIngredientes().stream()
                .allMatch(ri -> Boolean.TRUE.equals(ri.getIngrediente().getSinGluten()));
        boolean rapida = receta.getTiempo() <= TIEMPO_RAPIDO_MAX;
        boolean economica = coste.compareTo(COSTE_ECONOMICO_MAX.multiply(raciones)) <= 0;

        return new CalculoReceta(
                coste.setScale(2, RoundingMode.HALF_UP),
                coste.divide(raciones, 2, RoundingMode.HALF_UP),
                vegetariana, sinGluten, rapida, economica);
    }

    private RecetaDetalleResponse construirDetalle(Receta receta) {
        return recetaMapper.toDetalle(receta, calcular(receta));
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + id));
    }

    private RecursoNoEncontradoException noEncontrada(Long id) {
        return new RecursoNoEncontradoException("No existe la receta con id " + id);
    }
}