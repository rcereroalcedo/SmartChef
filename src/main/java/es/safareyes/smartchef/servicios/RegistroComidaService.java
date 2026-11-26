package es.safareyes.smartchef.servicios;

import es.safareyes.smartchef.dto.request.RegistroComidaRequest;
import es.safareyes.smartchef.dto.response.RegistroComidaResponse;
import es.safareyes.smartchef.dto.response.SemanaResponse;

import java.time.LocalDate;

public interface RegistroComidaService {

    RegistroComidaResponse registrar(Long usuarioId, RegistroComidaRequest request);

    SemanaResponse semana(Long usuarioId, LocalDate fecha);
}