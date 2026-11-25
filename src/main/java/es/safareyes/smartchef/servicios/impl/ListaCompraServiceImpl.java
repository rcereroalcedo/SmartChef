package es.safareyes.smartchef.servicios.impl;

import es.safareyes.smartchef.dto.request.CerrarListaRequest;
import es.safareyes.smartchef.dto.request.DesdeRecetaRequest;
import es.safareyes.smartchef.dto.response.ListaCompraResponse;
import es.safareyes.smartchef.excepciones.AccesoDenegadoException;
import es.safareyes.smartchef.excepciones.ConflictoException;
import es.safareyes.smartchef.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchef.excepciones.ReglaNegocioException;
import es.safareyes.smartchef.mapper.ListaCompraMapper;
import es.safareyes.smartchef.modelos.Despensa;
import es.safareyes.smartchef.modelos.EstadoListaCompra;
import es.safareyes.smartchef.modelos.Ingrediente;
import es.safareyes.smartchef.modelos.LineaListaCompra;
import es.safareyes.smartchef.modelos.ListaCompra;
import es.safareyes.smartchef.modelos.Receta;
import es.safareyes.smartchef.modelos.RecetaIngrediente;
import es.safareyes.smartchef.modelos.Usuario;
import es.safareyes.smartchef.repositorios.IDespensaRepository;
import es.safareyes.smartchef.repositorios.IListaCompraRepository;
import es.safareyes.smartchef.repositorios.IRecetaIngredienteRepository;
import es.safareyes.smartchef.repositorios.IRecetaRepository;
import es.safareyes.smartchef.repositorios.IUsuarioRepository;
import es.safareyes.smartchef.servicios.ListaCompraService;
import es.safareyes.smartchef.util.Fechas;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ListaCompraServiceImpl implements ListaCompraService {

    private final IListaCompraRepository listaCompraRepository;
    private final IRecetaRepository recetaRepository;
    private final IRecetaIngredienteRepository recetaIngredienteRepository;
    private final IDespensaRepository despensaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ListaCompraMapper listaCompraMapper;

    /** RN-05: escalar a las raciones pedidas, restar la despensa y sumar a la lista abierta. */
    @Override
    @Transactional
    public ListaCompraResponse desdeReceta(Long usuarioId, DesdeRecetaRequest request) {
        Usuario usuario = buscarUsuario(usuarioId);
        Receta receta = recetaRepository.findByIdAndActivaTrue(request.recetaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la receta con id " + request.recetaId()));

        List<RecetaIngrediente> lineasReceta =
                recetaIngredienteRepository.findConIngredientes(List.of(receta.getId()));

        // Lo que hay en casa (los caducados no cuentan)
        Map<Long, BigDecimal> enDespensa = despensaRepository.findNoCaducados(usuarioId, Fechas.hoy()).stream()
                .collect(Collectors.toMap(d -> d.getIngrediente().getId(), Despensa::getCantidad, BigDecimal::add));

        // Lo que falta, escalado a las raciones pedidas
        BigDecimal factor = BigDecimal.valueOf(request.raciones())
                .divide(BigDecimal.valueOf(receta.getRaciones()), 6, RoundingMode.HALF_UP);
        Map<Ingrediente, BigDecimal> faltantes = new LinkedHashMap<>();
        for (RecetaIngrediente ri : lineasReceta) {
            BigDecimal necesaria = ri.getCantidad().multiply(factor).setScale(2, RoundingMode.HALF_UP);
            BigDecimal falta = necesaria.subtract(
                    enDespensa.getOrDefault(ri.getIngrediente().getId(), BigDecimal.ZERO));
            if (falta.signum() > 0) {
                faltantes.put(ri.getIngrediente(), falta);
            }
        }
        if (faltantes.isEmpty()) {
            throw new ReglaNegocioException("Ya tienes en la despensa todos los ingredientes de esta receta");
        }

        // Solo una lista abierta por persona: se reutiliza si existe
        ListaCompra lista = listaCompraRepository
                .findFirstByUsuarioIdAndEstado(usuarioId, EstadoListaCompra.ABIERTA)
                .orElseGet(() -> {
                    ListaCompra nueva = new ListaCompra();
                    nueva.setUsuario(usuario);
                    nueva.setEstado(EstadoListaCompra.ABIERTA);
                    return nueva;
                });

        faltantes.forEach((ingrediente, cantidad) -> sumarALista(lista, ingrediente, cantidad));

        ListaCompra guardada = listaCompraRepository.saveAndFlush(lista);   // flush: así las líneas nuevas ya tienen id
        log.info("Usuario {} añade {} ingredientes de la receta {} a su lista {}",
                usuarioId, faltantes.size(), receta.getId(), guardada.getId());
        return listaCompraMapper.toResponse(guardada);
    }

    /** RN-06: lo comprado pasa a la despensa y la lista queda CERRADA. */
    @Override
    @Transactional
    public ListaCompraResponse cerrar(Long listaId, Long usuarioId, CerrarListaRequest request) {
        ListaCompra lista = listaCompraRepository.findById(listaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la lista con id " + listaId));

        if (!lista.getUsuario().getId().equals(usuarioId)) {
            throw new AccesoDenegadoException("Esta lista no es tuya");
        }
        if (lista.getEstado() != EstadoListaCompra.ABIERTA) {
            throw new ConflictoException("La lista ya está cerrada");
        }

        if (request != null && request.lineasCompradas() != null) {
            Set<Long> indicadas = new HashSet<>(request.lineasCompradas());
            Set<Long> deLaLista = lista.getLineas().stream()
                    .map(LineaListaCompra::getId).collect(Collectors.toSet());
            if (!deLaLista.containsAll(indicadas)) {
                throw new ReglaNegocioException("Alguna de las líneas indicadas no pertenece a la lista");
            }
            lista.getLineas().stream()
                    .filter(l -> indicadas.contains(l.getId()))
                    .forEach(l -> l.setComprado(true));
        }

        int pasadas = 0;
        for (LineaListaCompra linea : lista.getLineas()) {
            if (Boolean.TRUE.equals(linea.getComprado())) {
                sumarADespensa(lista.getUsuario(), linea);
                pasadas++;
            }
        }

        lista.setEstado(EstadoListaCompra.CERRADA);
        log.info("Usuario {} cierra la lista {}: {} líneas pasan a la despensa", usuarioId, listaId, pasadas);
        return listaCompraMapper.toResponse(lista);
    }

    private void sumarALista(ListaCompra lista, Ingrediente ingrediente, BigDecimal cantidad) {
        Optional<LineaListaCompra> existente = lista.getLineas().stream()
                .filter(l -> l.getIngrediente().getId().equals(ingrediente.getId()))
                .findFirst();
        if (existente.isPresent()) {
            LineaListaCompra linea = existente.get();
            linea.setCantidad(linea.getCantidad().add(cantidad));
        } else {
            LineaListaCompra nueva = new LineaListaCompra();
            nueva.setLista(lista);
            nueva.setIngrediente(ingrediente);
            nueva.setCantidad(cantidad);
            nueva.setComprado(false);
            lista.getLineas().add(nueva);
        }
    }

    private void sumarADespensa(Usuario usuario, LineaListaCompra linea) {
        Despensa despensa = despensaRepository
                .findByUsuarioIdAndIngredienteId(usuario.getId(), linea.getIngrediente().getId())
                .orElseGet(() -> {
                    Despensa nueva = new Despensa();
                    nueva.setUsuario(usuario);
                    nueva.setIngrediente(linea.getIngrediente());
                    nueva.setCantidad(BigDecimal.ZERO);
                    return nueva;
                });
        despensa.setCantidad(despensa.getCantidad().add(linea.getCantidad()));
        despensaRepository.save(despensa);
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + id));
    }
}