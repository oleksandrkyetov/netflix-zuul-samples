package olky.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SlowServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(SlowServiceApplication.class, args);
    }

}
