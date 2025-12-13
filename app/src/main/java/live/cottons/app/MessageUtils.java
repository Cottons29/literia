package live.cottons.app;

import live.cottons.TemplateGenerator;

import java.util.ArrayList;

public class MessageUtils implements TemplateGenerator {
    String message = "Hello World!";
    String ignore = "ignore";

    public MessageUtils setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public ArrayList<String> ignoreFields() {
        return new ArrayList<>() {{ add("ignore"); }};
    }
}
