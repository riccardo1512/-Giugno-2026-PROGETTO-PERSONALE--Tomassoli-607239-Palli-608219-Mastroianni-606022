package it.uniroma3.siw.negozio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.negozio.model.Credentials;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    Credentials findByUsername(String username);

}