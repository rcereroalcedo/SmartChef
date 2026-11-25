package es.safareyes.smartchef.mapper;

import es.safareyes.smartchef.dto.request.DespensaRequest;
import es.safareyes.smartchef.dto.response.DespensaResponse;
import es.safareyes.smartchef.modelos.Despensa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DespensaMapper {

    @Mapping(target = "ingredienteId", source = "ingrediente.id")
    @Mapping(target = "nombre", source = "ingrediente.nombre")
    @Mapping(target = "unidad", source = "ingrediente.tipoCantidad")
    @Mapping(target = "diasParaCaducar",
            expression = "java(d.getCaducidad() == null ? null : java.time.temporal.ChronoUnit.DAYS.between(es.safareyes.smartchef.util.Fechas.hoy(), d.getCaducidad()))")
    DespensaResponse toResponse(Despensa d);

    List<DespensaResponse> toResponseList(List<Despensa> lista);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "ingrediente", ignore = true)
    void actualizar(DespensaRequest request, @MappingTarget Despensa despensa);
}