package es.safareyes.smartchef.mapper;

import es.safareyes.smartchef.dto.response.IngredienteFaltanteResponse;
import es.safareyes.smartchef.dto.response.RecetaPropuestaResponse;
import es.safareyes.smartchef.modelos.Receta;
import es.safareyes.smartchef.modelos.RecetaIngrediente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PortadaMapper.class)
public interface PropuestaMapper {

    @Mapping(target = "fotoPortada", source = "receta.fotos", qualifiedByName = "urlPortada")
    @Mapping(target = "porcentajeCoincidencia", source = "coincidencia.porcentaje")
    @Mapping(target = "ingredientesDisponibles", source = "coincidencia.disponibles")
    @Mapping(target = "ingredientesTotales", source = "coincidencia.total")
    @Mapping(target = "faltan", source = "coincidencia.faltan")
    RecetaPropuestaResponse toPropuesta(Receta receta, Coincidencia coincidencia);

    @Mapping(target = "ingredienteId", source = "ingrediente.id")
    @Mapping(target = "nombre", source = "ingrediente.nombre")
    @Mapping(target = "unidad", source = "ingrediente.tipoCantidad")
    IngredienteFaltanteResponse toFaltante(RecetaIngrediente linea);
}