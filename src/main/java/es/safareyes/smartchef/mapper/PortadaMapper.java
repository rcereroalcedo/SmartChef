package es.safareyes.smartchef.mapper;

import es.safareyes.smartchef.modelos.Foto;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PortadaMapper {

    @Named("urlPortada")
    public String urlPortada(List<Foto> fotos) {
        if (fotos == null) return null;
        return fotos.stream()
                .filter(f -> Boolean.TRUE.equals(f.getEsPortada()))
                .map(Foto::getUrl)
                .findFirst()
                .orElse(null);
    }
}