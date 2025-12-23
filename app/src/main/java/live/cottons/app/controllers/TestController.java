package live.cottons.app.controllers;

import live.cottons.app.MessageUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class TestController {

    @GetMapping("/home")
    public Object home() {
        return "home";
    }

    @GetMapping("/tasks")
    public Object task() {
        return "tasks";
    }

    @GetMapping("/profile")
    public Object profile() {
        return "profile";
    }

    @GetMapping("/upload")
    public Object upload() {
        return "upload";
    }

}
