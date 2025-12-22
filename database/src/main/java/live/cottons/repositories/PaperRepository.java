package live.cottons.repositories;

import live.cottons.models.Paper;
import live.cottons.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaperRepository extends JpaRepository<Paper, Long> {
    List<Paper> findByAuthor(User author);
    List<Paper> findByStatus(Paper.Status status);
}
