package live.cottons.app.services;

import live.cottons.models.Paper;
import live.cottons.models.Review;
import live.cottons.models.User;
import live.cottons.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PaperService paperService;

    @Transactional
    public Review assignReviewer(Paper paper, User reviewer) {
        Review review = Review.builder()
                .paper(paper)
                .reviewer(reviewer)
                .reviewedAt(null)
                .build();
        paperService.updatePaperStatus(paper.getId(), Paper.Status.UNDER_REVIEW);
        return reviewRepository.save(review);
    }

    @Transactional
    public void submitReview(Long reviewId, Integer score, String comments) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setScore(score);
        review.setComments(comments);
        review.setReviewedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    public List<Review> getReviewsByReviewer(User reviewer) {
        return reviewRepository.findByReviewer(reviewer);
    }

    public List<Review> getReviewsByPaper(Paper paper) {
        return reviewRepository.findByPaper(paper);
    }
}
