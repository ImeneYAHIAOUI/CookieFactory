package fr.unice.polytech.cucumber.client;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.client.UnregisteredClient;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.interfaces.AgendaProcessor;
import fr.unice.polytech.interfaces.CartHandler;
import fr.unice.polytech.interfaces.CookieRegistration;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.*;


 public class ModifyQuantityInCartStepDefs {
    Client client;
    final List<Ingredient> codIngredients = new ArrayList<>();
    final List<Cookie> cookieList = new ArrayList<>();
    @Autowired
    CookieRegistration cookieRegistration;
    @Autowired
    AgendaProcessor agendaProcessor;
    @Autowired
    CartHandler cartHandler;
    @Given("^a  store with recipes")
    public void andGiven(DataTable table) throws CookieException {
        codIngredients.add(new Topping(UUID.randomUUID(), "chocolate chips", 1));
        codIngredients.add(new Topping(UUID.randomUUID(),"m&ms", 1));
        codIngredients.add(new Flavour(UUID.randomUUID(),"caramel", 1));
        codIngredients.add(new Dough(UUID.randomUUID(),"chocolate dough", 1));
        codIngredients.add(new Flavour(UUID.randomUUID(),"vanillaFlavour", 1));
        codIngredients.add(new Flavour(UUID.randomUUID(),"strawberryFlavour", 1));
        codIngredients.add(new Flavour(UUID.randomUUID(),"chocolateFlavour", 1));
        codIngredients.add(new Topping(UUID.randomUUID(),"white chocolate chips", 1));
        List<List<String>> rows = table.asLists(String.class);

        for (List<String> row : rows) {
            List<Topping> toppings = new ArrayList<>();
            Dough dough = (Dough) codIngredients
                    .stream()
                    .filter(i -> i.getName().equals(row.get(1)))
                    .findFirst()
                    .orElse(null);
            Flavour flavor = (Flavour) codIngredients
                    .stream()
                    .filter(i -> i.getName().equals(row.get(2)))
                    .findFirst()
                    .orElse(null);
            for (int j = 3; j < row.size(); j++) {
                int finalJ = j;
                toppings.add(((Topping) codIngredients
                        .stream()
                        .filter(i -> i.getName().equals(row.get(finalJ)))
                        .findFirst()
                        .orElse(null)));
            }
            Cookie cookie = new SimpleCookieBuilder()
                    .setName(row.get(0))
                    .setPrice(1.)
                    .setCookingTime(30)
                    .setCooking(Cooking.CHEWY)
                    .setMix(Mix.TOPPED)
                    .setDough(dough)
                    .setFlavour(flavor)
                    .setToppings(toppings)
                    .build();
            cookieList.add(cookie);
            cookieRegistration.suggestRecipe(cookie);
            cookieRegistration.acceptRecipe(cookie,1.);
        }
    }
    @Given("unregistered client  with phone number {string}")
    public void clientInTest(String numberPhone)throws InvalidPhoneNumberException {
        client=new UnregisteredClient(numberPhone);
    }
    @Given("registered client  with phone number {string}")
    public void clientRInTest(String numberPhone){
        client=new RegisteredClient("sd",numberPhone,"ds");

    }
    @When("the client choose {int} cookie with name {string}")
    public void chooseCookie(int count, String name){
        Cookie cookie=cookieList.stream().filter(recipe -> recipe.getName().equals(name)).findFirst().orElse(null);

        client.getCart().setPickupTime(LocalTime.parse("10:00").atDate(LocalDate.now(agendaProcessor.getClock())));

        cartHandler.addItem(new Item(count, cookie), client.getCart());
    }
    @Then("in his cart there is {int} cookie with name {string}")
    public void cookieInCart(int count, String name){
        assertEquals(1, client.getCart().getItems().size());
        Item item= client.getCart().getItems().stream().filter(item1-> item1.getCookie().getName().equals(name)).findFirst().orElse(null);
        assertNotNull(item);
        assertEquals(item.getQuantity(), count);
        assertTrue(client.getCart().getTotal()==count*item.getCookie().getPrice());
    }
    @When("he add another {int} cookie with name {string}")
    public void AddtheSameCookie(int count, String name){
        Cookie cookie=cookieList.stream().filter(recipe -> recipe.getName().equals(name)).findFirst().orElse(null);
        cartHandler.addItem(new Item(count, cookie), client.getCart());
    }
    @Then("in his cart he has {int} cookies with name {string}")
    public void cookeiNumber(int count, String name){
        cookieInCart(count,name);
    }
    @When("he remove all items in cart")
     public void clearCart(){
        cartHandler.clearCart(client.getCart());
   }
    @Then("his cart is empty")
     public void cartIsEmpty(){
        assertTrue(client.getCart().getItems().isEmpty());
        assertTrue(client.getCart().getTotal()==0.0);
    }
    @When("he remove {int} cookie with name {string}")
     public void removeQuantity(int count , String  name){
        Item item= client.getCart().getItems().stream().filter(item1-> item1.getCookie().getName().equals(name)).findFirst().orElse(null);
        cartHandler.removeItem(item,client.getCart(),count);
    }
     @When("in his cart he has {int} cookie with name {string}")
     public void QuantityRemoved(int count , String  name){
         assertEquals(1, client.getCart().getItems().size());
         Item item= client.getCart().getItems().stream().filter(item1-> item1.getCookie().getName().equals(name)).findFirst().orElse(null);
         assertNotNull(item);
         assertEquals(item.getQuantity(), count);
         assertTrue(client.getCart().getTotal()==count*item.getCookie().getPrice());
     }

}
