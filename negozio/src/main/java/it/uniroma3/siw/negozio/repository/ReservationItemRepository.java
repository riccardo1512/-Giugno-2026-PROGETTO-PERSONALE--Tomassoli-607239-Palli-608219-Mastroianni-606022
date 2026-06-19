package it.uniroma3.siw.negozio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.negozio.model.ReservationItem;

public interface ReservationItemRepository extends JpaRepository<ReservationItem, Long> {

}