package it.uniroma3.siw.negozio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.negozio.model.Reservation;
import it.uniroma3.siw.negozio.model.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser(User user);
    
    java.util.Optional<Reservation> findByUserAndState(User user, it.uniroma3.siw.negozio.model.ReservationState state);
    
    List<Reservation> findByUserAndStateNot(User user, it.uniroma3.siw.negozio.model.ReservationState state);
}