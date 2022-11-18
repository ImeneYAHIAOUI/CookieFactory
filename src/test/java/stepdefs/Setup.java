package stepdefs;

import fr.unice.polytech.cod.COD;
import io.cucumber.java.Before;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

/**
 * Utility class to hold the setup code for the Cucumber tests.
 */
public class Setup {
    @Before(value = "not @same-cod", order = 0)
    public void before() {
        COD.reset();
        String instantExpected = "2022-11-07T09:15:00Z";
        Clock clock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));
        COD.setCLOCK(clock);
    }
}
