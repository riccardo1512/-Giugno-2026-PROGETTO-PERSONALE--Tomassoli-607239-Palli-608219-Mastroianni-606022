package it.uniroma3.siw.negozio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.negozio.model.Review;

import it.uniroma3.siw.negozio.model.CD;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.cd = :cd")
    Double getAverageRatingForCD(@Param("cd") CD cd);
}
