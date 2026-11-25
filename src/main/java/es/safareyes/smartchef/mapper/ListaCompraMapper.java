package es.safareyes.smartchef.mapper;

import es.safareyes.smartchef.dto.response.LineaListaCompraResponse;
import es.safareyes.smartchef.dto.response.ListaCompraResponse;
import es.safareyes.smartchef.modelos.LineaListaCompra;
import es.safareyes.smartchef.modelos.ListaCompra;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ListaCompraMapper {

    ListaCompraResponse toResponse(ListaCompra lista);

    @Mapping(target = "ingredienteId", source = "ingrediente.id")
    @Mapping(target = "nombre", source = "ingrediente.nombre")
    @Mapping(target = "unidad", source = "ingrediente.tipoCantidad")
    LineaListaCompraResponse toLineaResponse(LineaListaCompra linea);
}