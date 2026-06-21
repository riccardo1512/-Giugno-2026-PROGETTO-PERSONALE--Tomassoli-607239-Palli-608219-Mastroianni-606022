package it.uniroma3.siw.negozio.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.negozio.model.Author;
import it.uniroma3.siw.negozio.repository.AuthorRepository;

@Component
public class AuthorValidator implements Validator {

    private final AuthorRepository authorRepository;

    public AuthorValidator(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Author.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Author author = (Author) target;
        boolean duplicate = author.getId() == null
                ? authorRepository.existsByNameAndSurname(author.getName(), author.getSurname())
                : authorRepository.existsByNameAndSurnameAndIdNot(author.getName(), author.getSurname(), author.getId());
        if (duplicate) {
            errors.reject("duplicate.author", "Esiste già un autore con questo nome e cognome");
        }
    }
}
