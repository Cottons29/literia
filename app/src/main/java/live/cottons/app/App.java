package live.cottons.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "live.cottons")
public class App {
    static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
