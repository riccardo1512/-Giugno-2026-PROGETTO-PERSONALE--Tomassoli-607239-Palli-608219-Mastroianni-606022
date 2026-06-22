package it.uniroma3.siw.negozio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.negozio.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
