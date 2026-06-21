package it.uniroma3.siw.negozio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.negozio.model.ReservationItem;

import java.util.List;
import it.uniroma3.siw.negozio.model.CD;

public interface ReservationItemRepository extends JpaRepository<ReservationItem, Long> {
    List<ReservationItem> findByCd(CD cd);
}