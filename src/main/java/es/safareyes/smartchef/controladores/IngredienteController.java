package es.safareyes.smartchef.controladores;

import es.safareyes.smartchef.dto.response.IngredienteResponse;
import es.safareyes.smartchef.servicios.IngredienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingredientes")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService ingredienteService;

    @GetMapping
    public Page<IngredienteResponse> buscar(
            @RequestParam(defaultValue = "") String texto,
            @PageableDefault(size = 20, sort = "nombre") Pageable pageable) {
        return ingredienteService.buscar(texto, pageable);
    }
}