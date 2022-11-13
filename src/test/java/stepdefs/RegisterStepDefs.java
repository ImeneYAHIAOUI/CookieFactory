package stepdefs;

import fr.unice.polytech.COD;
import fr.unice.polytech.exception.RegistrationException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Objects;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RegisterStepDefs {
    COD cod;
    String id;
    String mdp;
    String phoneNumber;

    @Given("an empty cod without data")
    public void andGivenAnEmptyCOD() {
        cod = new COD();
    }

    @When("Client register with id {string}, mdp {string}, phone number {string}")
    public void registerClient(String id, String mdp, String phoneNumber) {
        this.id = id;
        this.mdp = mdp;
        this.phoneNumber = phoneNumber;
    }

    @Then("Cod clients is not empty")
    public void thenCodClientsNotEmpty() {
        assertFalse(cod.getClients().isEmpty());
    }

    @And("Cod client contains client with id {string}, mdp {string}, phone number {string}")
    public void codClientContainsClientWithIdMdpPhoneNumber(String id, String mdp, String phoneNumber) {
        assertTrue(cod.getClients().stream().anyMatch(c -> c.getId().equals(id) && c.getPassword().equals(mdp) && Objects.equals(c.getPhoneNumber(), phoneNumber)));
    }

    @And("Cod client contains {int} element")
    public void codClientContainsElement(int nb) {
        assertEquals(cod.getClients().size(), nb);
    }

    @Then("this client can't register")
    public void thenCookiesNotAvailable() {
        assertThrows(RegistrationException.class, () -> cod.register(id, mdp, phoneNumber));
    }

    @Then("this client can register")
    public void thenCookiesAvailable() {
        assertDoesNotThrow(() -> cod.register(id, mdp, phoneNumber));
    }
}
