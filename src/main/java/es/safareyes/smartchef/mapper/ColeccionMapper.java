package es.safareyes.smartchef.mapper;

import es.safareyes.smartchef.dto.response.ColeccionResponse;
import es.safareyes.smartchef.dto.response.RecetaEnColeccionResponse;
import es.safareyes.smartchef.modelos.Coleccion;
import es.safareyes.smartchef.modelos.Receta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = PortadaMapper.class)
public interface ColeccionMapper {

    ColeccionResponse toResponse(Coleccion coleccion);

    List<ColeccionResponse> toResponseList(List<Coleccion> colecciones);

    @Mapping(target = "fotoPortada", source = "fotos", qualifiedByName = "urlPortada")
    @Mapping(target = "retirada", expression = "java(!Boolean.TRUE.equals(r.getActiva()))")
    RecetaEnColeccionResponse toRecetaResponse(Receta r);
}