package fr.unice.polytech.cucumber.client;

import fr.unice.polytech.components.ClientManager;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.exception.InvalidInputException;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.RegistrationException;
import fr.unice.polytech.interfaces.ClientHandler;
import fr.unice.polytech.repositories.ClientRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.StreamSupport;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class LogInStepDefs  {
    String mail;
    String password;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientHandler client;
    @Given("repository client with registered client with mail {string} , password {string} and phone number {string}")
    public void codWithRegisteredClientWithIdAndPassword(String mail, String password, String phoneNumber) throws RegistrationException ,InvalidPhoneNumberException {
            clientRepository.deleteAll();
            assertTrue(StreamSupport.stream(clientRepository.findAll().spliterator(), false).count()==0);
            client.register(mail, password, phoneNumber);
            this.mail = mail;
            this.password = password;
            assertTrue(StreamSupport.stream(clientRepository.findAll().spliterator(), false).count()==1);

    }
    @When("Client log in  with mail {string} and password {string}")
    public void client_log_in_with_id_and_password(String id, String password) {

        assertDoesNotThrow(() -> {
            client.logIn(id, password);
        });
    }
    @Then("Client Manager connected clients is not empty")
    public void Client_Manager_connected_clients_is_not_empty() {
        assertFalse(((ClientManager) client).getConnectedClients().isEmpty());
    }
    @And("this client can logIn")
    public void this_client_can_log_in() {
        RegisteredClient cl = ((ClientManager) client).getConnectedClients().get(0);
        assertEquals(cl.getMail(), mail);
        assertEquals(cl.getPassword(),password);
    }

    @When("Client log in with invalid password :  mail {string} and password {string}")
    public void client_log_in_with_invalid_password_id_and_password(String mail, String password) {
        client.logOff(((ClientManager) client).getConnectedClients().get(0));
        assertThrows(InvalidInputException.class, ()->client.logIn(mail, password));

    }
    @When("Client log in with invalid id :  mail {string} and password {string}")
    public void client_log_in_with_invalid_id_id_and_password(String mail, String password){
        assertThrows(InvalidInputException.class, ()->client.logIn(mail, password));
    }
    @Then("Client Manager connected clients is empty")
    public void Client_Manager_connected_clients_is_empty() {
        assertTrue(((ClientManager) client).getConnectedClients().isEmpty());
    }
}
