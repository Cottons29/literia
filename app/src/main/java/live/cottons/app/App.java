package live.cottons.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "live.cottons")
@EnableJpaRepositories("live.cottons.repositories")
@EntityScan("live.cottons.models")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
