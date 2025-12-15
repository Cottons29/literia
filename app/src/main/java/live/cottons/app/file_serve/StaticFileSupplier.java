package live.cottons.app.file_serve;


import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

@Controller
public class StaticFileSupplier implements WebMvcConfigurer {
    @GetMapping(path = "/supplier/static")
    public ResponseEntity<Resource> getAssets(@RequestParam String filename, @RequestParam String type, @RequestParam(defaultValue = "false") boolean asset) {
        String safeFilename = Objects.requireNonNullElse(filename, "").replace("\\", "/");
        if (safeFilename.isBlank() || safeFilename.contains("..") || safeFilename.startsWith("/")) {
            return ResponseEntity.badRequest().build();
        }

        String filePath = "static/" + safeFilename + "." + type;
        if (asset) {
            filePath = "static/assets/" + safeFilename + "." + type;
        }

        System.out.println("Image Path" + filePath);

        // Serve from classpath: src/main/resources/scripts/js/{filename}
        Resource resource = new ClassPathResource(filePath);
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        // Disable client/proxy caching to always fetch from disk
        HttpHeaders headers = new HttpHeaders();

        headers.setCacheControl(CacheControl.noStore().mustRevalidate());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        switch (type) {
            case "js":
                headers.add("Content-Type", "application/javascript");
                break;
            case "css":
                headers.add("Content-Type", "text/css");
                break;
            case "png" , "jpeg", "jpg":
                headers.add("Content-Type", "image/" + type);
                break;
        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

}
