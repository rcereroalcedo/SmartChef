package es.safareyes.smartchef.servicios.impl;

import es.safareyes.smartchef.dto.request.RegistroComidaRequest;
import es.safareyes.smartchef.dto.response.DiaSemanaResponse;
import es.safareyes.smartchef.dto.response.RegistroComidaResponse;
import es.safareyes.smartchef.dto.response.SemanaResponse;
import es.safareyes.smartchef.excepciones.ConflictoException;
import es.safareyes.smartchef.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchef.excepciones.ReglaNegocioException;
import es.safareyes.smartchef.mapper.RegistroComidaMapper;
import es.safareyes.smartchef.modelos.Despensa;
import es.safareyes.smartchef.modelos.Receta;
import es.safareyes.smartchef.modelos.RecetaIngrediente;
import es.safareyes.smartchef.modelos.RegistroComida;
import es.safareyes.smartchef.modelos.Usuario;
import es.safareyes.smartchef.repositorios.IDespensaRepository;
import es.safareyes.smartchef.repositorios.IRecetaIngredienteRepository;
import es.safareyes.smartchef.repositorios.IRecetaRepository;
import es.safareyes.smartchef.repositorios.IRegistroComidaRepository;
import es.safareyes.smartchef.repositorios.IUsuarioRepository;
import es.safareyes.smartchef.servicios.RegistroComidaService;
import es.safareyes.smartchef.util.Fechas;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RegistroComidaServiceImpl implements RegistroComidaService {

    private final IRegistroComidaRepository registroComidaRepository;
    private final IRecetaRepository recetaRepository;
    private final IRecetaIngredienteRepository recetaIngredienteRepository;
    private final IDespensaRepository despensaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final RegistroComidaMapper registroComidaMapper;

    /** RN-07: un registro por día y momento, sin fechas futuras, con descuento opcional de la despensa. */
    @Override
    @Transactional
    public RegistroComidaResponse registrar(Long usuarioId, RegistroComidaRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId));

        boolean sinReceta = request.recetaId() == null;
        if (sinReceta && (request.descripcion() == null || request.descripcion().isBlank())) {
            throw new ReglaNegocioException("Indica una receta o describe la comida");
        }
        if (sinReceta && request.descontarDespensa()) {
            throw new ReglaNegocioException("Para descontar de la despensa hay que indicar una receta");
        }
        if (registroComidaRepository.existsByUsuarioIdAndFechaAndMomento(
                usuarioId, request.fecha(), request.momento())) {
            throw new ConflictoException("Ya tienes un registro para ese día y ese momento");
        }

        Receta receta = null;
        if (!sinReceta) {
            receta = recetaRepository.findByIdAndActivaTrue(request.recetaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe la receta con id " + request.recetaId()));
        }

        RegistroComida registro = registroComidaMapper.toEntity(request);
        registro.setUsuario(usuario);
        registro.setReceta(receta);

        if (request.descontarDespensa()) {
            descontarDeDespensa(usuarioId, receta);
        }

        RegistroComida guardado = registroComidaRepository.save(registro);
        log.info("Usuario {} registra {} del {}", usuarioId, request.momento(), request.fecha());
        return registroComidaMapper.toResponse(guardado);
    }

    /** La semana (lunes a domingo) que contiene la fecha, con las calorías de cada día. */
    @Override
    public SemanaResponse semana(Long usuarioId, LocalDate fecha) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId));

        LocalDate referencia = fecha != null ? fecha : Fechas.hoy();
        LocalDate lunes = referencia.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate domingo = lunes.plusDays(6);

        Map<LocalDate, List<RegistroComida>> porDia = registroComidaRepository
                .findSemana(usuarioId, lunes, domingo).stream()
                .collect(Collectors.groupingBy(RegistroComida::getFecha));

        List<DiaSemanaResponse> dias = new ArrayList<>();
        int caloriasSemana = 0;
        for (int i = 0; i < 7; i++) {
            LocalDate dia = lunes.plusDays(i);
            List<RegistroComida> delDia = porDia.getOrDefault(dia, List.of()).stream()
                    .sorted(Comparator.comparing(RegistroComida::getMomento))
                    .toList();
            int calorias = delDia.stream()
                    .filter(r -> r.getReceta() != null && r.getReceta().getCalorias() != null)
                    .mapToInt(r -> r.getReceta().getCalorias())
                    .sum();
            caloriasSemana += calorias;
            dias.add(new DiaSemanaResponse(dia, calorias, registroComidaMapper.toResponseList(delDia)));
        }
        return new SemanaResponse(lunes, domingo, caloriasSemana, dias);
    }

    /** Resta lo gastado sin bajar de cero; si llega a cero, el ingrediente sale de la despensa. */
    private void descontarDeDespensa(Long usuarioId, Receta receta) {
        List<RecetaIngrediente> lineas = recetaIngredienteRepository.findConIngredientes(List.of(receta.getId()));
        for (RecetaIngrediente ri : lineas) {
            Optional<Despensa> enDespensa = despensaRepository
                    .findByUsuarioIdAndIngredienteId(usuarioId, ri.getIngrediente().getId());
            enDespensa.ifPresent(d -> {
                BigDecimal restante = d.getCantidad().subtract(ri.getCantidad());
                if (restante.signum() <= 0) {
                    despensaRepository.delete(d);
                } else {
                    d.setCantidad(restante);
                }
            });
        }
    }
}