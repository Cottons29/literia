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

    @GetMapping("/index")
    public Object test(
            Model model,
            @RequestParam(required = false, defaultValue = "html") String format,
            @Param("name") String name
    ) {
        if ( format.equalsIgnoreCase("json")) {
            return new ResponseEntity<>(Map.of("message", "Hello World! This is Json Data"), HttpStatus.OK);
        }
        new MessageUtils().setMessage(name).buildModel(model);
        return "index";
    }

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
