package it.uniroma3.siw.negozio.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.negozio.model.User;
import it.uniroma3.siw.negozio.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User getUser(Long id) {
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

}
