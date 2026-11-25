package es.safareyes.smartchef.controladores;

import es.safareyes.smartchef.dto.request.BuscarPorIngredientesRequest;
import es.safareyes.smartchef.dto.response.RecetaPropuestaResponse;
import es.safareyes.smartchef.servicios.PropuestaRecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recetas")
@RequiredArgsConstructor
public class PropuestaRecetaController {

    private static final String CABECERA_USUARIO = "X-Usuario-Id";

    private final PropuestaRecetaService propuestaRecetaService;

    @PostMapping("/buscar-por-ingredientes")
    public List<RecetaPropuestaResponse> buscarPorIngredientes(
            @Valid @RequestBody(required = false) BuscarPorIngredientesRequest request,
            @RequestHeader(CABECERA_USUARIO) Long usuarioId) {

        BuscarPorIngredientesRequest peticion =
                request != null ? request : new BuscarPorIngredientesRequest(null, null);
        return propuestaRecetaService.buscarPorIngredientes(usuarioId, peticion);
    }
}