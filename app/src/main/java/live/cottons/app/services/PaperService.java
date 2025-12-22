package live.cottons.app.services;

import live.cottons.models.Paper;
import live.cottons.models.User;
import live.cottons.repositories.PaperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaperService {
    private final PaperRepository paperRepository;

    @Transactional
    public Paper submitPaper(String title, String paperAbstract, String filePath, User author) {
        Paper paper = Paper.builder()
                .title(title)
                .paperAbstract(paperAbstract)
                .filePath(filePath)
                .author(author)
                .status(Paper.Status.SUBMITTED)
                .submittedAt(LocalDateTime.now())
                .build();
        return paperRepository.save(paper);
    }

    public List<Paper> getPapersByAuthor(User author) {
        return paperRepository.findByAuthor(author);
    }

    public List<Paper> getAllPapers() {
        return paperRepository.findAll();
    }

    public Paper getPaperById(Long id) {
        return paperRepository.findById(id).orElseThrow(() -> new RuntimeException("Paper not found"));
    }

    @Transactional
    public void updatePaperStatus(Long id, Paper.Status status) {
        Paper paper = getPaperById(id);
        paper.setStatus(status);
        paperRepository.save(paper);
    }
}
