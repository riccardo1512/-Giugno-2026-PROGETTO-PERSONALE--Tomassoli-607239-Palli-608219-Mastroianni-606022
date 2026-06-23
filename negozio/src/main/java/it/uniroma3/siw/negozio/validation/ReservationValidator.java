package it.uniroma3.siw.negozio.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.negozio.model.Reservation;

@Component
public class ReservationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Reservation.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Reservation reservation = (Reservation) target;
        
        if (reservation.getItems() == null || reservation.getItems().isEmpty()) {
            errors.rejectValue("items", "EmptyCart", "Il carrello non può essere vuoto");
        }
    }
}
