package live.cottons.app.controllers;

import live.cottons.app.services.PaperService;
import live.cottons.app.services.ReviewService;
import live.cottons.app.services.UserService;
import live.cottons.models.Paper;
import live.cottons.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PaperService paperService;
    private final UserService userService;
    private final ReviewService reviewService;

    @GetMapping("/papers")
    public String managePapers(Model model) {
        model.addAttribute("papers", paperService.getAllPapers());
        // Simple way to get all reviewers - this could be optimized
        List<User> reviewers = userService.findAll().stream()
                .filter(u -> u.getRoles().contains(User.Role.REVIEWER))
                .collect(Collectors.toList());
        model.addAttribute("reviewers", reviewers);
        return "admin_papers";
    }

    @PostMapping("/assign")
    public String assignReviewer(@RequestParam Long paperId, @RequestParam Long reviewerId) {
        Paper paper = paperService.getPaperById(paperId);
        User reviewer = userService.findById(reviewerId);
        reviewService.assignReviewer(paper, reviewer);
        return "redirect:/admin/papers";
    }

    @PostMapping("/decide")
    public String decidePaper(@RequestParam Long paperId, @RequestParam Paper.Status status) {
        paperService.updatePaperStatus(paperId, status);
        return "redirect:/admin/papers";
    }
}
