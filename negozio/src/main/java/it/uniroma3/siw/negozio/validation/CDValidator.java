package it.uniroma3.siw.negozio.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.negozio.model.CD;
import it.uniroma3.siw.negozio.repository.CDRepository;

@Component
public class CDValidator implements Validator {

    private final CDRepository cdRepository;

    public CDValidator(CDRepository cdRepository) {
        this.cdRepository = cdRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CD.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CD cd = (CD) target;
        if (cd.getAuthor() == null) {
            errors.rejectValue("author", "required", "L'autore è obbligatorio");
            return;
        }
        
        boolean duplicate = cd.getId() == null
                ? cdRepository.existsByNameAndAuthor(cd.getName(), cd.getAuthor())
                : cdRepository.existsByNameAndAuthorAndIdNot(cd.getName(), cd.getAuthor(), cd.getId());
        if (duplicate) {
            errors.reject("duplicate.cd", "Esiste già un CD con questo titolo per questo autore");
        }
    }
}
