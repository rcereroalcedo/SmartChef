package es.safareyes.smartchef.mapper;

import es.safareyes.smartchef.dto.request.RegistroComidaRequest;
import es.safareyes.smartchef.dto.response.RegistroComidaResponse;
import es.safareyes.smartchef.modelos.RegistroComida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RegistroComidaMapper {

    @Mapping(target = "recetaId", source = "receta.id")
    @Mapping(target = "recetaNombre", source = "receta.nombre")
    @Mapping(target = "calorias", source = "receta.calorias")
    RegistroComidaResponse toResponse(RegistroComida registro);

    List<RegistroComidaResponse> toResponseList(List<RegistroComida> registros);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)   // lo pone el servicio
    @Mapping(target = "receta", ignore = true)    // lo pone el servicio
    @Mapping(target = "tipo", ignore = true)
    RegistroComida toEntity(RegistroComidaRequest request);
}