package es.safareyes.smartchef.controladores;

import es.safareyes.smartchef.dto.request.FiltroRecetas;
import es.safareyes.smartchef.dto.request.RecetaActualizarRequest;
import es.safareyes.smartchef.dto.request.RecetaRequest;
import es.safareyes.smartchef.dto.response.RecetaDetalleResponse;
import es.safareyes.smartchef.dto.response.RecetaResumenResponse;
import es.safareyes.smartchef.modelos.Dificultad;
import es.safareyes.smartchef.servicios.RecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/recetas")
@RequiredArgsConstructor
public class RecetaController {

    // Temporal: se sustituye por el usuario del token JWT en la fase de seguridad
    private static final String CABECERA_USUARIO = "X-Usuario-Id";

    private final RecetaService recetaService;

    @GetMapping
    public Page<RecetaResumenResponse> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Dificultad dificultad,
            @RequestParam(required = false) Integer caloriasMax,
            @RequestParam(required = false) Integer tiempoMax,
            @RequestParam(defaultValue = "false") boolean vegetariana,
            @RequestParam(defaultValue = "false") boolean sinGluten,
            @RequestParam(defaultValue = "false") boolean rapida,
            @RequestParam(defaultValue = "false") boolean economica,
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable) {

        FiltroRecetas filtro = new FiltroRecetas(nombre, dificultad, caloriasMax, tiempoMax,
                vegetariana, sinGluten, rapida, economica);
        return recetaService.listar(filtro, pageable);
    }

    @GetMapping("/{id}")
    public RecetaDetalleResponse obtener(@PathVariable Long id) {
        return recetaService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<RecetaDetalleResponse> crear(
            @Valid @RequestBody RecetaRequest request,
            @RequestHeader(CABECERA_USUARIO) Long usuarioId) {

        RecetaDetalleResponse creada = recetaService.crear(request, usuarioId);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(ubicacion).body(creada);   // 201 + cabecera Location
    }

    @PutMapping("/{id}")
    public RecetaDetalleResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RecetaActualizarRequest request,
            @RequestHeader(CABECERA_USUARIO) Long usuarioId) {
        return recetaService.actualizar(id, request, usuarioId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void retirar(@PathVariable Long id,
                        @RequestHeader(CABECERA_USUARIO) Long usuarioId) {
        recetaService.retirar(id, usuarioId);
    }
}