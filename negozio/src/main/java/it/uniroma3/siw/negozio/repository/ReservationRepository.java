package it.uniroma3.siw.negozio.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.negozio.model.Reservation;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

}