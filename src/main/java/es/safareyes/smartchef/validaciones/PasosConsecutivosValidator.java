package es.safareyes.smartchef.validaciones;

import es.safareyes.smartchef.dto.request.PasoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class PasosConsecutivosValidator implements ConstraintValidator<PasosConsecutivos, List<PasoRequest>> {

    @Override
    public boolean isValid(List<PasoRequest> pasos, ConstraintValidatorContext contexto) {
        if (pasos == null || pasos.isEmpty()) return true;   // de eso se encarga @NotEmpty
        if (pasos.stream().anyMatch(p -> p == null || p.numero() == null)) return true;  // y de esto @NotNull

        List<Integer> numeros = pasos.stream().map(PasoRequest::numero).sorted().toList();
        for (int i = 0; i < numeros.size(); i++) {
            if (!numeros.get(i).equals(i + 1)) return false;
        }
        return true;
    }
}