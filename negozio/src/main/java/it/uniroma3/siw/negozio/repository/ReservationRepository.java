package it.uniroma3.siw.negozio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.negozio.model.Reservation;
import it.uniroma3.siw.negozio.model.ReservationState;
import it.uniroma3.siw.negozio.model.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser(User user);
    
    java.util.Optional<Reservation> findByUserAndState(User user, ReservationState state);
    
    List<Reservation> findByUserAndStateNot(User user, ReservationState state);
    
    @Query("SELECT r FROM Reservation r LEFT JOIN FETCH r.items i LEFT JOIN FETCH i.cd WHERE r.user = :user AND r.state != :state")
    List<Reservation> findByUserAndStateNotWithItemsAndCds(@Param("user") User user, @Param("state") ReservationState state);
}