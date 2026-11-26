package es.safareyes.smartchef.controladores;

import es.safareyes.smartchef.dto.request.RegistroComidaRequest;
import es.safareyes.smartchef.dto.response.RegistroComidaResponse;
import es.safareyes.smartchef.dto.response.SemanaResponse;
import es.safareyes.smartchef.servicios.RegistroComidaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/registros")
@RequiredArgsConstructor
public class RegistroComidaController {

    private static final String CABECERA_USUARIO = "X-Usuario-Id";

    private final RegistroComidaService registroComidaService;

    @PostMapping
    public ResponseEntity<RegistroComidaResponse> registrar(
            @Valid @RequestBody RegistroComidaRequest request,
            @RequestHeader(CABECERA_USUARIO) Long usuarioId) {

        RegistroComidaResponse creado = registroComidaService.registrar(usuarioId, request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.id()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @GetMapping("/semana")
    public SemanaResponse semana(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestHeader(CABECERA_USUARIO) Long usuarioId) {
        return registroComidaService.semana(usuarioId, fecha);
    }
}