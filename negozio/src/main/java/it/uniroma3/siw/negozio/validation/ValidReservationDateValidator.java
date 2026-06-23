package it.uniroma3.siw.negozio.validation;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidReservationDateValidator implements ConstraintValidator<ValidReservationDate, LocalDate> {

    @Override
    public void initialize(ValidReservationDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // Let @NotNull handle null values
        }
        // Valid if date is not in the future
        return !date.isAfter(LocalDate.now());
    }
}
