package es.safareyes.smartchef.servicios.impl;

import es.safareyes.smartchef.dto.response.ColeccionResponse;
import es.safareyes.smartchef.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchef.excepciones.ReglaNegocioException;
import es.safareyes.smartchef.mapper.ColeccionMapper;
import es.safareyes.smartchef.modelos.Coleccion;
import es.safareyes.smartchef.modelos.Receta;
import es.safareyes.smartchef.modelos.Usuario;
import es.safareyes.smartchef.repositorios.IColeccionRepository;
import es.safareyes.smartchef.repositorios.IRecetaRepository;
import es.safareyes.smartchef.repositorios.IUsuarioRepository;
import es.safareyes.smartchef.servicios.ColeccionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ColeccionServiceImpl implements ColeccionService {

    private static final String FAVORITAS = "Favoritas";
    private static final int NOMBRE_MAX = 80;

    private final IColeccionRepository coleccionRepository;
    private final IRecetaRepository recetaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ColeccionMapper coleccionMapper;

    @Override
    @Transactional
    public List<ColeccionResponse> listar(Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        asegurarFavoritas(usuario);
        return coleccionMapper.toResponseList(coleccionRepository.findConRecetas(usuarioId));
    }

    @Override
    @Transactional
    public ColeccionResponse guardarReceta(Long usuarioId, String nombreColeccion, Long recetaId) {
        String nombre = nombreColeccion == null ? "" : nombreColeccion.trim();
        if (nombre.isEmpty() || nombre.length() > NOMBRE_MAX) {
            throw new ReglaNegocioException(
                    "El nombre de la colección debe tener entre 1 y " + NOMBRE_MAX + " caracteres");
        }

        Usuario usuario = buscarUsuario(usuarioId);
        Receta receta = recetaRepository.findByIdAndActivaTrue(recetaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la receta con id " + recetaId));

        Coleccion coleccion = coleccionRepository.findByUsuarioIdAndNombreIgnoreCase(usuarioId, nombre)
                .orElseGet(() -> nuevaColeccion(usuario, nombre));

        coleccion.getRecetas().add(receta);   // es un Set: si ya estaba, no se repite

        Coleccion guardada = coleccionRepository.save(coleccion);
        log.info("Usuario {} guarda la receta {} en la colección '{}'", usuarioId, recetaId, guardada.getNombre());
        return coleccionMapper.toResponse(guardada);
    }

    private void asegurarFavoritas(Usuario usuario) {
        if (!coleccionRepository.existsByUsuarioIdAndNombreIgnoreCase(usuario.getId(), FAVORITAS)) {
            coleccionRepository.save(nuevaColeccion(usuario, FAVORITAS));
        }
    }

    private Coleccion nuevaColeccion(Usuario usuario, String nombre) {
        Coleccion coleccion = new Coleccion();
        coleccion.setUsuario(usuario);
        coleccion.setNombre(nombre);
        return coleccion;
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + id));
    }
}