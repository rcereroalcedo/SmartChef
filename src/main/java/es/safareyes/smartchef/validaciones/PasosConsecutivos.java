package es.safareyes.smartchef.validaciones;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasosConsecutivosValidator.class)
@Target({ElementType.FIELD, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasosConsecutivos {

    String message() default "Los pasos deben estar numerados 1, 2, 3... sin huecos ni repetidos";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}