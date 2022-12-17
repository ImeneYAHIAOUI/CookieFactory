package fr.unice.polytech.cucumber.order;

import fr.unice.polytech.interfaces.InventoryFiller;
import fr.unice.polytech.interfaces.InventoryUpdater;
import fr.unice.polytech.interfaces.NotificationHandler;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@CucumberContextConfiguration
@SpringBootTest
public class OrderRunCucumberConfig {
    @MockBean
    private InventoryFiller inventoryManager;
    @MockBean
    private InventoryUpdater inventoryUpdater;
    @SpyBean
    private NotificationHandler notificationManager;
}
