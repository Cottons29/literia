package live.cottons.app;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


}
