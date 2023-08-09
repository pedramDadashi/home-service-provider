package ir.maktabsharif.homeservicephase2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HomeServicePhase2Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run
                = SpringApplication.run(HomeServicePhase2Application.class, args);
    }
}
