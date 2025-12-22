package live.cottons.app.controllers;

import live.cottons.app.services.PaperService;
import live.cottons.app.services.UserService;
import live.cottons.models.Paper;
import live.cottons.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/papers")
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;
    private final UserService userService;

    private final String UPLOAD_DIR = "uploads/";

    @GetMapping("/submit")
    public String submitPage() {
        return "upload";
    }

    @PostMapping("/submit")
    public String submitPaper(@RequestParam String title,
                              @RequestParam String paperAbstract,
                              @RequestParam("file") MultipartFile file,
                              @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        
        User author = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        paperService.submitPaper(title, paperAbstract, path.toString(), author);

        return "redirect:/home";
    }

    @GetMapping("/my-papers")
    public String myPapers(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User author = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("papers", paperService.getPapersByAuthor(author));
        return "tasks";
    }
}
