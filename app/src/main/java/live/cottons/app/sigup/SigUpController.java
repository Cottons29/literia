package live.cottons.sigup;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
// import live.cottons.liter.sigup.SignUpFrom;

@Controller
@RequestMapping("/auth")
public class SigUpController {

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signUpForm", new SignUpFrom());
        return "signup"; // page that includes this fragment
    }

    @PostMapping("/signup")
public String signup(@ModelAttribute SignUpRequest form) {

    System.out.println(form.getName());
    System.out.println(form.getEmail());
    System.out.println(form.getPassword());

    return "redirect:/login";
}

}
