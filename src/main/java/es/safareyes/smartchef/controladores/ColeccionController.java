package es.safareyes.smartchef.controladores;

import es.safareyes.smartchef.dto.response.ColeccionResponse;
import es.safareyes.smartchef.servicios.ColeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colecciones")
@RequiredArgsConstructor
public class ColeccionController {

    private static final String CABECERA_USUARIO = "X-Usuario-Id";

    private final ColeccionService coleccionService;

    @GetMapping
    public List<ColeccionResponse> listar(@RequestHeader(CABECERA_USUARIO) Long usuarioId) {
        return coleccionService.listar(usuarioId);
    }

    @PutMapping("/{nombre}/recetas/{recetaId}")
    public ColeccionResponse guardarReceta(
            @PathVariable String nombre,
            @PathVariable Long recetaId,
            @RequestHeader(CABECERA_USUARIO) Long usuarioId) {
        return coleccionService.guardarReceta(usuarioId, nombre, recetaId);
    }
}