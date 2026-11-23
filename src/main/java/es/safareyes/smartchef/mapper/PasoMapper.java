package es.safareyes.smartchef.mapper;

import es.safareyes.smartchef.dto.request.PasoRequest;
import es.safareyes.smartchef.dto.response.PasoResponse;
import es.safareyes.smartchef.modelos.Paso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PasoMapper {

    PasoResponse toResponse(Paso paso);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receta", ignore = true)   // lo enlaza RecetaMapper al crear la receta
    Paso toEntity(PasoRequest request);
}