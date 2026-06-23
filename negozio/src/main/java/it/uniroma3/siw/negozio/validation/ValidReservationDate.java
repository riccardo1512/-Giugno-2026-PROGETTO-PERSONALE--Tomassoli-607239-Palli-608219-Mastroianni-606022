package it.uniroma3.siw.negozio.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidReservationDateValidator.class)
public @interface ValidReservationDate {
    
    String message() default "La data della prenotazione non può essere nel futuro";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
