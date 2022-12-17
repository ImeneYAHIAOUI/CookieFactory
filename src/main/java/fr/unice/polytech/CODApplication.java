package fr.unice.polytech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CODApplication {
    public static void main(String[] args) {
        SpringApplication.run(CODApplication.class, args);
    }
}
