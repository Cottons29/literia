package live.cottons.app.file_serve;


import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

@Controller
public class StaticFileSupplier implements WebMvcConfigurer {
    @GetMapping(path = "/res/scripts",produces = "application/javascript")
    public ResponseEntity<Resource> getJs(@RequestParam String filename) {
        String safeFilename = Objects.requireNonNullElse(filename, "").replace("\\", "/");
        if (safeFilename.isBlank() || safeFilename.contains("..") || safeFilename.startsWith("/")) {
            return ResponseEntity.badRequest().build();
        }

        // Serve from classpath: src/main/resources/scripts/js/{filename}
        Resource resource = new ClassPathResource("scripts/js/" + safeFilename);
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resource);
    }

}
