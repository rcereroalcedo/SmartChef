package es.safareyes.smartchef.servicios.impl;

import es.safareyes.smartchef.dto.response.InformeRecetasPopularesResponse;
import es.safareyes.smartchef.excepciones.ReglaNegocioException;
import es.safareyes.smartchef.repositorios.IColeccionRepository;
import es.safareyes.smartchef.repositorios.IRegistroComidaRepository;
import es.safareyes.smartchef.servicios.InformeService;
import es.safareyes.smartchef.util.Fechas;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InformeServiceImpl implements InformeService {

    private static final int TOP = 10;
    private static final int DIAS_POR_DEFECTO = 30;

    private final IRegistroComidaRepository registroComidaRepository;
    private final IColeccionRepository coleccionRepository;

    @Override
    public InformeRecetasPopularesResponse recetasPopulares(LocalDate desde, LocalDate hasta) {
        LocalDate fin = hasta != null ? hasta : Fechas.hoy();
        LocalDate inicio = desde != null ? desde : fin.minusDays(DIAS_POR_DEFECTO);
        if (inicio.isAfter(fin)) {
            throw new ReglaNegocioException("La fecha 'desde' no puede ser posterior a 'hasta'");
        }

        Pageable top = PageRequest.of(0, TOP);
        log.info("Informe de recetas populares del {} al {}", inicio, fin);
        return new InformeRecetasPopularesResponse(
                inicio, fin,
                registroComidaRepository.findMasCocinadas(inicio, fin, top),
                coleccionRepository.findMasGuardadas(top));
    }
}