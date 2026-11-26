package es.safareyes.smartchef.dto.request;

import es.safareyes.smartchef.modelos.MomentoComida;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegistroComidaRequest(
        @NotNull(message = "La fecha es obligatoria")
        @PastOrPresent(message = "No se pueden registrar fechas futuras")
        LocalDate fecha,

        @NotNull(message = "El momento es obligatorio")
        MomentoComida momento,

        Long recetaId,

        @Size(max = 200, message = "La descripción no puede pasar de 200 caracteres")
        String descripcion,

        Boolean descontarDespensa
) {
        public RegistroComidaRequest {
                if (descontarDespensa == null) {
                        descontarDespensa = false;
                }
        }
}