package it.uniroma3.siw.negozio.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.negozio.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

}
