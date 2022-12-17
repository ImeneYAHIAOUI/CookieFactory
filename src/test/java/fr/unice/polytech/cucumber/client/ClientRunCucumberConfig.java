package fr.unice.polytech.cucumber.client;

import fr.unice.polytech.components.TooGoodToGoManager;
import fr.unice.polytech.interfaces.NotificationHandler;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@CucumberContextConfiguration
@SpringBootTest
public class ClientRunCucumberConfig {
    @SpyBean
    TooGoodToGoManager tooGoodToGo;

    @MockBean
    ScheduledThreadPoolExecutor tooGoodToGoExecutor;

    @SpyBean
    NotificationHandler notificationHandler;
}
