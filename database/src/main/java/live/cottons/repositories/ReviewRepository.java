package live.cottons.repositories;

import live.cottons.models.Review;
import live.cottons.models.User;
import live.cottons.models.Paper;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewer(User reviewer);
    List<Review> findByPaper(Paper paper);
}
