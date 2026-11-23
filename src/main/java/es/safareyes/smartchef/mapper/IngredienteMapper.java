package es.safareyes.smartchef.mapper;

import es.safareyes.smartchef.dto.response.IngredienteResponse;
import es.safareyes.smartchef.modelos.Ingrediente;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredienteMapper {

    IngredienteResponse toResponse(Ingrediente ingrediente);

    List<IngredienteResponse> toResponseList(List<Ingrediente> ingredientes);
}