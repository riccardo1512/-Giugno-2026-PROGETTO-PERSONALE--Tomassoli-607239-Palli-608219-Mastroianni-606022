package it.uniroma3.siw.negozio.authentication;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.negozio.model.Credentials;
import it.uniroma3.siw.negozio.model.User;
import it.uniroma3.siw.negozio.repository.CredentialsRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(CredentialsRepository credentialsRepository, PasswordEncoder passwordEncoder) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Facciamo caricare l'utente base a Spring Security
        OAuth2User oauth2User = super.loadUser(userRequest);

        // 2. Capire da quale provider arriva (Google o GitHub)
        String clientRegistrationId = userRequest.getClientRegistration().getRegistrationId();

        String email = null;
        String name = null;

        if ("google".equalsIgnoreCase(clientRegistrationId)) {
            email = oauth2User.getAttribute("email");
            name = oauth2User.getAttribute("name");
        } else if ("github".equalsIgnoreCase(clientRegistrationId)) {
            // Su GitHub la mail potrebbe non essere esposta pubblicamente, usiamo il "login"
            email = oauth2User.getAttribute("login") + "@github.com"; 
            name = oauth2User.getAttribute("name");
            if (name == null) {
                name = oauth2User.getAttribute("login"); // fallback se il nome non è impostato
            }
        }

        // 3. Controlliamo se questo utente esiste già nel nostro database
        if (email != null) {
            Credentials credenzialiEsistenti = credentialsRepository.findByUsername(email);

            // 4. Se non esiste, lo creiamo!
            if (credenzialiEsistenti == null) {
                User nuovoUtente = new User();
                
                // Dividiamo il nome in "Nome" e "Cognome" se possibile
                if (name != null && name.contains(" ")) {
                    String[] parti = name.split(" ", 2);
                    nuovoUtente.setName(parti[0]);
                    nuovoUtente.setSurname(parti[1]);
                } else {
                    nuovoUtente.setName(name != null ? name : "Sconosciuto");
                    nuovoUtente.setSurname("OAuth2");
                }
                
                // Usiamo l'email come username, dato che deve essere unico e non vuoto
                nuovoUtente.setUsername(email);

                Credentials nuoveCredenziali = new Credentials();
                nuoveCredenziali.setUser(nuovoUtente);
                nuoveCredenziali.setUsername(email);
                
                // Generiamo una password finta impossibile da indovinare, 
                // in modo che l'utente non possa fare login manuale, ma solo tramite OAuth2
                String fakePassword = UUID.randomUUID().toString();
                nuoveCredenziali.setPassword(passwordEncoder.encode(fakePassword));
                
                // Diamo il ruolo base
                nuoveCredenziali.setRole(Credentials.DEFAULT_ROLE);

                // Salviamo nel DB
                credentialsRepository.save(nuoveCredenziali);
            }
        }

        return oauth2User;
    }
}
