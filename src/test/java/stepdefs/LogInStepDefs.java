package stepdefs;

import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.cod.COD;
import fr.unice.polytech.exception.InvalidInputException;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.RegistrationException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

public class LogInStepDefs {
    COD cod;
    String id;
    String password;

    @Given("cod with registered client with id {string} , password {string} and phone number {string}")
    public void codWithRegisteredClientWithIdAndPassword(String id, String password, String phoneNumber) {
        cod = COD.getInstance();
        try {
            cod.register(id, password, phoneNumber);
            this.id = id;
            this.password = password;
        } catch (RegistrationException | InvalidPhoneNumberException ignored) {

        }
    }
    @When("Client log in  with id {string} and password {string}")
    public void client_log_in_with_id_and_password(String id, String password) {
        try {
            cod.logIn(id, password);
        } catch (InvalidInputException exception) {

        }
    }
    @Then("Cod connected clients is not empty")
    public void cod_connected_clients_is_not_empty() {
        assertFalse(cod.getConnectedClients().isEmpty());
    }
    @And("this client can logIn")
    public void this_client_can_log_in() {
        RegisteredClient client = cod.getConnectedClients().get(0);
        assertEquals(client.getId(),id);
        assertEquals(client.getPassword(),password);
    }

    @When("Client log in with invalid password :  id {string} and password {string}")
    public void client_log_in_with_invalid_password_id_and_password(String id, String password) {
        try {
            cod.logIn(id, password);
        } catch (InvalidInputException ignored) {

        }
    }
    @When("Client log in with invalid id :  id {string} and password {string}")
    public void client_log_in_with_invalid_id_id_and_password(String id, String password) {
        try {
            cod.logIn(id, password);
        }catch (InvalidInputException exception) {

        }
    }
    @Then("Cod connected clients is empty")
    public void cod_connected_clients_is_empty() {
        assertTrue(cod.getConnectedClients().isEmpty());
    }
}
