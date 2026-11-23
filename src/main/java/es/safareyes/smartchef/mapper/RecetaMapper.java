package es.safareyes.smartchef.mapper;

import es.safareyes.smartchef.dto.request.RecetaActualizarRequest;
import es.safareyes.smartchef.dto.request.RecetaRequest;
import es.safareyes.smartchef.dto.response.IngredienteRecetaResponse;
import es.safareyes.smartchef.dto.response.RecetaDetalleResponse;
import es.safareyes.smartchef.dto.response.RecetaResumenResponse;
import es.safareyes.smartchef.modelos.Foto;
import es.safareyes.smartchef.modelos.Receta;
import es.safareyes.smartchef.modelos.RecetaIngrediente;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PasoMapper.class, FotoMapper.class})
public interface RecetaMapper {

    // ---------- Entidad -> Response ----------

    @Mapping(target = "autor", source = "autor.nombre")                          // aplanado
    @Mapping(target = "fotoPortada", source = "fotos", qualifiedByName = "urlPortada")  // calculado
    RecetaResumenResponse toResumen(Receta receta);

    List<RecetaResumenResponse> toResumenList(List<Receta> recetas);

    @Mapping(target = "costeTotal", ignore = true)
    @Mapping(target = "costePorRacion", ignore = true)
    @Mapping(target = "vegetariana", ignore = true)
    @Mapping(target = "sinGluten", ignore = true)
    @Mapping(target = "rapida", ignore = true)
    @Mapping(target = "economica", ignore = true)
    @Mapping(target = "autor", source = "receta.autor.nombre")
    RecetaDetalleResponse toDetalle(Receta receta, CalculoReceta calculo);

    @Mapping(target = "ingredienteId", source = "ingrediente.id")
    @Mapping(target = "nombre", source = "ingrediente.nombre")
    @Mapping(target = "unidad", source = "ingrediente.tipoCantidad")
    @Mapping(target = "vegetariano", source = "ingrediente.vegetariano")
    @Mapping(target = "sinGluten", source = "ingrediente.sinGluten")
    @Mapping(target = "coste",
            expression = "java(ri.getCantidad().multiply(ri.getIngrediente().getPrecio()))")
    IngredienteRecetaResponse toLineaResponse(RecetaIngrediente ri);

    // ---------- Request -> Entidad ----------

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "calorias", ignore = true)      // se calcula desde los ingredientes
    @Mapping(target = "activa", ignore = true)        // nace activa
    @Mapping(target = "autor", ignore = true)         // lo pone el servicio
    @Mapping(target = "fotos", ignore = true)         // se suben aparte
    @Mapping(target = "ingredientes", ignore = true)  // el servicio busca cada Ingrediente por id
    Receta toEntity(RecetaRequest request);

    // ---------- Actualización parcial ----------

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "calorias", ignore = true)
    @Mapping(target = "activa", ignore = true)
    @Mapping(target = "autor", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    @Mapping(target = "pasos", ignore = true)         // las listas las reemplaza el servicio
    @Mapping(target = "ingredientes", ignore = true)
    void actualizar(RecetaActualizarRequest request, @MappingTarget Receta receta);

    // ---------- Métodos de apoyo ----------

    @Named("urlPortada")
    default String urlPortada(List<Foto> fotos) {
        return fotos.stream()
                .filter(f -> Boolean.TRUE.equals(f.getEsPortada()))
                .map(Foto::getUrl)
                .findFirst()
                .orElse(null);
    }

    @AfterMapping
    default void enlazarPasos(@MappingTarget Receta receta) {
        receta.getPasos().forEach(paso -> paso.setReceta(receta));
    }
}