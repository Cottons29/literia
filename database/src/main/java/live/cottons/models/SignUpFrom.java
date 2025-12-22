package live.cottons.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import live.cottons.TemplateGenerator;

public class SignUpFrom implements TemplateGenerator {

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

}