package es.safareyes.smartchef.controladores;

import es.safareyes.smartchef.dto.request.CerrarListaRequest;
import es.safareyes.smartchef.dto.request.DesdeRecetaRequest;
import es.safareyes.smartchef.dto.response.ListaCompraResponse;
import es.safareyes.smartchef.servicios.ListaCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/listas-compra")
@RequiredArgsConstructor
public class ListaCompraController {

    private static final String CABECERA_USUARIO = "X-Usuario-Id";

    private final ListaCompraService listaCompraService;

    @PostMapping("/desde-receta")
    public ResponseEntity<ListaCompraResponse> desdeReceta(
            @Valid @RequestBody DesdeRecetaRequest request,
            @RequestHeader(CABECERA_USUARIO) Long usuarioId) {

        ListaCompraResponse lista = listaCompraService.desdeReceta(usuarioId, request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/listas-compra/{id}").buildAndExpand(lista.id()).toUri();
        return ResponseEntity.created(ubicacion).body(lista);
    }

    @PatchMapping("/{id}/cerrar")
    public ListaCompraResponse cerrar(
            @PathVariable Long id,
            @RequestBody(required = false) CerrarListaRequest request,
            @RequestHeader(CABECERA_USUARIO) Long usuarioId) {
        return listaCompraService.cerrar(id, usuarioId, request);
    }
}