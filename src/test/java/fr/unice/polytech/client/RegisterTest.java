package fr.unice.polytech.client;

import fr.unice.polytech.COD;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

public class RegisterTest {
    COD cod;

    @Given("an empty cod without data" )
    public void AndGiven()
    {
        cod=new COD();
    }

    @When("Client register with id {string}, mdp {string}, phone number {int}")
    public void registerClient(String id, String mdp, int phoneNumber){
        cod.register(id, mdp, phoneNumber);
    }

    @Then("Cod clients is not empty")
    public void ThenCodClientsNotEmpty(){
        assertFalse(cod.getClients().isEmpty());
    }

    @And("Cod client contains client with id {string}, mdp {string}, phone number {int}")
    public void codClientContainsClientWithIdMdpPhoneNumber(String id, String mdp, int phoneNumber) {
        RegisteredClient cli = (cod.getClients().stream().filter(c -> c.getId().equals(id) && c.getPassword().equals(mdp) && c.getPhoneNumber() == phoneNumber)).findFirst().orElse(null);
        assertNotNull(cli);
    }

    @And("Cod client contains {int} element")
    public void codClientContainsElement(int nb) {
        assertEquals(cod.getClients().size(), nb);
    }
}
