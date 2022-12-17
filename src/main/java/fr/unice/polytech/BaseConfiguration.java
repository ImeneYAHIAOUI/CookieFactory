package fr.unice.polytech;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.Clock;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
public class BaseConfiguration {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    @Scope("prototype")
    public ScheduledThreadPoolExecutor tooGoodToGoExecutor() {
        return new ScheduledThreadPoolExecutor(1);
    }
}
