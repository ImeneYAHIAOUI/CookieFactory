package fr.unice.polytech.cucumber.client;

import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.exception.RegistrationException;
import fr.unice.polytech.interfaces.ClientHandler;
import fr.unice.polytech.repositories.ClientRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class RegisterStepDefs {
    String id;
    String mdp;
    String phoneNumber;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientHandler client;

    @Given("an empty client repository")
    public void andGivenAnEmptyCOD() {
        clientRepository.deleteAll();
    }

    @When("Client register with mail {string}, mdp {string}, phone number {string}")
    public void registerClient(String mail, String mdp, String phoneNumber) {
        this.id = mail;
        this.mdp = mdp;
        this.phoneNumber = phoneNumber;
    }

    @Then("Cod clients is not empty")
    public void thenCodClientsNotEmpty() {
        assertNotEquals(0, clientRepository.count());
    }

    @And("client repository contains client with mail {string}, mdp {string}, phone number {string}")
    public void clientRepositoryContainsClientWithIdMdpPhoneNumber(String mail, String mdp, String phoneNumber) {

        assertTrue(StreamSupport.stream(clientRepository.findAll().spliterator(), false).anyMatch(c -> ((RegisteredClient)c).getMail().equals(mail) &&
                ((RegisteredClient)c).getPassword().equals(mdp) && Objects.equals(c.getPhoneNumber(), phoneNumber)));
    }

    @And("repository client contains {int} element")
    public void repositoryClientContainsElement(int nb) {
        assertEquals(clientRepository.count(), nb);
    }

    @Then("this client can't register")
    public void thenCookiesNotAvailable() {
        assertThrows(RegistrationException.class, () -> client.register(id, mdp, phoneNumber));
    }

    @Then("this client can register")

    public void thenCookiesAvailable() {
        assertDoesNotThrow(() -> client.register(id, mdp, phoneNumber));
    }

}
