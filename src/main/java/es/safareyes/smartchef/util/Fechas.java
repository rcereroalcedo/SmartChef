package es.safareyes.smartchef.util;

import java.time.LocalDate;
import java.time.ZoneId;

public final class Fechas {

    private static final ZoneId MADRID = ZoneId.of("Europe/Madrid");

    private Fechas() { }

    public static LocalDate hoy() {
        return LocalDate.now(MADRID);
    }
}