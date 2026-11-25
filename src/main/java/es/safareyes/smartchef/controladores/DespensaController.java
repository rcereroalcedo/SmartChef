package es.safareyes.smartchef.controladores;

import es.safareyes.smartchef.dto.request.DespensaRequest;
import es.safareyes.smartchef.dto.response.DespensaResponse;
import es.safareyes.smartchef.servicios.DespensaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/despensa")
@RequiredArgsConstructor
public class DespensaController {

    // Temporal: se sustituye por el usuario del token JWT en la fase de seguridad
    private static final String CABECERA_USUARIO = "X-Usuario-Id";

    private final DespensaService despensaService;

    @GetMapping
    public List<DespensaResponse> listar(@RequestHeader(CABECERA_USUARIO) Long usuarioId) {
        return despensaService.listar(usuarioId);
    }

    @PutMapping("/{ingredienteId}")
    public ResponseEntity<DespensaResponse> guardar(
            @PathVariable Long ingredienteId,
            @Valid @RequestBody DespensaRequest request,
            @RequestHeader(CABECERA_USUARIO) Long usuarioId) {

        return despensaService.guardar(usuarioId, ingredienteId, request)
                .map(ResponseEntity::ok)                                   // 200: añadida o actualizada
                .orElseGet(() -> ResponseEntity.noContent().build());      // 204: cantidad 0, eliminada
    }
}