package es.safareyes.smartchef.servicios;

import es.safareyes.smartchef.dto.response.InformeRecetasPopularesResponse;

import java.time.LocalDate;

public interface InformeService {

    InformeRecetasPopularesResponse recetasPopulares(LocalDate desde, LocalDate hasta);
}