package it.uniroma3.siw.negozio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.negozio.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}