package es.safareyes.smartchef.mapper;

import es.safareyes.smartchef.dto.response.FotoResponse;
import es.safareyes.smartchef.modelos.Foto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FotoMapper {

    FotoResponse toResponse(Foto foto);
}