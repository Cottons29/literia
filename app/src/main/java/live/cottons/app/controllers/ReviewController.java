package live.cottons.app.controllers;

import live.cottons.app.services.PaperService;
import live.cottons.app.services.ReviewService;
import live.cottons.app.services.UserService;
import live.cottons.models.Paper;
import live.cottons.models.Review;
import live.cottons.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final PaperService paperService;

    @GetMapping("/assigned")
    public String assignedReviews(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User reviewer = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("reviews", reviewService.getReviewsByReviewer(reviewer));
        return "reviews";
    }

    @PostMapping("/submit")
    public String submitReview(@RequestParam Long reviewId,
                               @RequestParam Integer score,
                               @RequestParam String comments) {
        reviewService.submitReview(reviewId, score, comments);
        return "redirect:/reviews/assigned";
    }
}
