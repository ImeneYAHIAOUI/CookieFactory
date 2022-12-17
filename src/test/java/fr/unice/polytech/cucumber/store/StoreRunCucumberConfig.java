package fr.unice.polytech.cucumber.store;

import fr.unice.polytech.interfaces.ILocationService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@CucumberContextConfiguration
@SpringBootTest
public class StoreRunCucumberConfig {

    @Autowired
    @MockBean
    ILocationService locationService;
}
