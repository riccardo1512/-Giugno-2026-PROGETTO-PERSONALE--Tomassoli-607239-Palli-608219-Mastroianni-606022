package it.uniroma3.siw.negozio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.negozio.model.Review;
import it.uniroma3.siw.negozio.repository.ReviewRepository;

import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Iterable<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Transactional
    public void delete(Review review) {
        reviewRepository.delete(review);
    }
}
