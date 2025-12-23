package live.cottons.app.controllers;

import live.cottons.app.services.PaperService;
import live.cottons.app.services.UserService;
import live.cottons.models.Paper;
import live.cottons.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/papers")
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;
    private final UserService userService;

    private final String UPLOAD_DIR = "uploads";

    @GetMapping("/submit")
    public String submitPage() {
        return "upload";
    }

    @PostMapping("/submit")
    public String submitPaper(@RequestParam String title,
                              @RequestParam("paperAbstract") String paperAbstract,
                              @RequestParam("file") MultipartFile file,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) throws IOException {

        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a PDF file.");
            return "redirect:/papers/submit";
        }

        String original = file.getOriginalFilename();
        String safeName = original == null ? "upload.pdf" : Paths.get(original).getFileName().toString();
        String lower = safeName.toLowerCase();

        if (!"application/pdf".equalsIgnoreCase(file.getContentType()) && !lower.endsWith(".pdf")) {
            redirectAttributes.addFlashAttribute("error", "Only PDF files are allowed.");
            return "redirect:/papers/submit";
        }

        long MAX_BYTES = 25L * 1024L * 1024L;
        if (file.getSize() > MAX_BYTES) {
            redirectAttributes.addFlashAttribute("error", "File too large (max 25MB).");
            return "redirect:/papers/submit";
        }

        String fileName = UUID.randomUUID().toString() + "_" + safeName;
        Path target = Paths.get(UPLOAD_DIR, fileName).toAbsolutePath();

        try {
            Files.createDirectories(target.getParent());
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save file.");
            return "redirect:/papers/submit";
        }

        User author = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        paperService.submitPaper(title, paperAbstract, target.toString(), author);

        redirectAttributes.addFlashAttribute("success", "Paper submitted successfully.");
        return "redirect:/home";
    }

    @GetMapping("/my-papers")
    public String myPapers(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User author = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("papers", paperService.getPapersByAuthor(author));
        return "tasks";
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<Resource> previewPaper(@PathVariable Long id) {
        Paper paper = paperService.getPaperById(id);
        Path filePath = Paths.get(paper.getFilePath());

        if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Resource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/pdf";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDisposition(ContentDisposition.inline()
                    .filename(filePath.getFileName().toString())
                    .build());

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}