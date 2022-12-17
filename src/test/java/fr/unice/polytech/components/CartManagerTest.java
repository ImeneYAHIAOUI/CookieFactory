package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.store.TimeSlot;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.PickupTimeNotSetException;
import fr.unice.polytech.interfaces.AgendaProcessor;
import fr.unice.polytech.interfaces.CartHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class CartManagerTest {
    private  Cart cart;
    private Cookie cookie;
    private Cookie cookie1;
    private Item item1;
    private Item item;
    @Autowired
    CartHandler cartManager;
    @Autowired
    AgendaProcessor agendaProcessor;
    @BeforeEach
    public void setUp()throws CookieException {
        cart=new Cart();
        cart.setPickupTime(LocalDateTime.now(Clock.systemDefaultZone()));
         cookie= new SimpleCookieBuilder()
                .setName("chocolala")
                .setPrice(1.)
                .setCookingTime(2)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(new Dough(UUID.randomUUID(),"chocolate", 1.))
                .setFlavour(new Flavour(UUID.randomUUID(),"chocolate", 1.))
                .build();
        cookie1= new SimpleCookieBuilder()
                .setName("chocolala")
                .setPrice(2.)
                .setCookingTime(2)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(new Dough(UUID.randomUUID(),"chocolate", 1.))
                .setFlavour(new Flavour(UUID.randomUUID(),"chocolate", 1.))
                .build();
    }
    @Test
    public void addItemTest1() {

        Item item=new Item(2,cookie);
        assertTrue(cart.getItems().isEmpty());
        assertTrue(cart.getTotal().equals(0.0));
        assertTrue(cart.getTax().equals(0.0));
        cartManager.addItem(item,cart);
        assertTrue(cart.getItems().stream().count()==1);
        assertTrue(cart.getItems().stream().count()==1);
        assertTrue(cart.getItems().get(0).equals(item));
        assertTrue(cart.getItems().get(0).getQuantity()==2);
        assertTrue(cart.getTotal()==cookie.getPrice()*2);
    }

    @Test
    public void addItemTest2() {

         item=new Item(2,cookie);
        assertTrue(cart.getItems().isEmpty());
        assertTrue(cart.getTotal().equals(0.0));
        assertTrue(cart.getTax().equals(0.0));
        cartManager.addItem(item,cart);
        assertTrue(cart.getItems().stream().count()==1);
        assertTrue(cart.getItems().get(0).equals(item));
        assertTrue(cart.getItems().get(0).getQuantity()==2);
        assertTrue(cart.getTotal()==cookie.getPrice()*2);
          item1=new Item(3,cookie);
        cartManager.addItem(item1,cart);
        assertTrue(cart.getItems().stream().count()==1);
        assertTrue(cart.getItems().get(0).getQuantity()==5);
        assertTrue(cart.getTotal()==cookie.getPrice()*5);
    }
    @Test
    public void removeItemTest() {

        item=new Item(2,cookie);
        assertTrue(cart.getItems().isEmpty());
        assertTrue(cart.getTotal().equals(0.0));
        assertTrue(cart.getTax().equals(0.0));
        cartManager.addItem(item,cart);
        assertTrue(cart.getItems().stream().count()==1);
        assertTrue(cart.getItems().get(0).equals(item));
        assertTrue(cart.getItems().get(0).getQuantity()==2);
        assertTrue(cart.getTotal()==cookie.getPrice()*2);
        cartManager.removeItem(item,cart,2);
        assertTrue(cart.getItems().stream().count()==0);
        assertTrue(cart.getTotal()==0.0);
    }
    @Test
    public void removeItemTest2() {

        item=new Item(2,cookie);
        assertTrue(cart.getItems().isEmpty());
        assertTrue(cart.getTotal().equals(0.0));
        assertTrue(cart.getTax().equals(0.0));
        cartManager.addItem(item,cart);
        assertTrue(cart.getItems().stream().count()==1);
        assertTrue(cart.getItems().get(0).equals(item));
        assertTrue(cart.getItems().get(0).getQuantity()==2);
        assertTrue(cart.getTotal()==cookie.getPrice()*2);
        cartManager.removeItem(item,cart,1);
        assertTrue(cart.getItems().stream().count()==1);
        assertTrue(cart.getItems().get(0).getQuantity()==1);
        assertTrue(cart.getTotal()==cookie.getPrice()*1);
    }
    @Test
    public void addItemTest3() {

         item=new Item(2,cookie);
        assertTrue(cart.getItems().isEmpty());
        assertTrue(cart.getTotal().equals(0.0));
        assertTrue(cart.getTax().equals(0.0));
        cartManager.addItem(item,cart);
        assertTrue(cart.getItems().stream().count()==1);
        assertTrue(cart.getItems().get(0).equals(item));
        assertTrue(cart.getItems().get(0).getQuantity()==2);
        assertTrue(cart.getTotal()==cookie.getPrice()*2);
        item1=new Item(3,cookie1);
        cartManager.addItem(item1,cart);
        assertTrue(cart.getItems().stream().count()==2);
        assertTrue(cart.getItems().get(0).getQuantity()==2);
        assertTrue(cart.getItems().get(1).getQuantity()==3);
        assertTrue(cart.getTotal()==8.0);
    }
    @Test
    public void calculateAccordingPriceTest1(){
        item1=new Item(3,cookie1);
        item=new Item(2,cookie);
        cartManager.addItem(item,cart);
        assertTrue(cart.getTotal()==cookie.getPrice()*2);
        cartManager.addItem(item1,cart);
        assertTrue(cart.getTotal()==8.0);

    }
    @Test
    public void calculateAccordingPriceTest2(){
        item1=new Item(3,cookie1);
        item=new Item(2,cookie);
        cart.setTax(1.0);
        cartManager.addItem(item,cart);
        assertTrue(cart.getTotal()==2.02);
        cartManager.addItem(item1,cart);
        assertTrue(cart.getTotal()==8.08);

    }
    @Test
    public void totalCookingTime1(){
        item1=new Item(3,cookie1);
        item=new Item(2,cookie);
        cartManager.addItem(item,cart);
        cartManager.addItem(item1,cart);
        Duration duration=cartManager.totalCookingTime(cart);
        assertTrue(duration.toMinutes()==11);
    }
    public void getEstimatedTimeSlotTest() {
        item1=new Item(3,cookie1);
        item=new Item(2,cookie);
        cartManager.addItem(item,cart);
        cartManager.addItem(item1,cart);
        assertThrows(PickupTimeNotSetException.class , ()->cartManager.getEstimatedTimeSlot(cart));
    }
    public void getEstimatedTimeSlotTest1() throws PickupTimeNotSetException{
        item1=new Item(3,cookie1);
        item=new Item(2,cookie);
        cartManager.addItem(item,cart);
        cartManager.addItem(item1,cart);
        Duration duration=cartManager.totalCookingTime(cart);
        cart.setPickupTime(LocalTime.parse("10:00").atDate(LocalDate.now(agendaProcessor.getClock())));
        assertTrue(duration.toMinutes()==11);
        TimeSlot timeSlot=cartManager.getEstimatedTimeSlot(cart);
    }
    @Test
    public void clearCartTest(){
        item1=new Item(3,cookie1);
        item=new Item(2,cookie);
        cartManager.addItem(item,cart);
        cartManager.addItem(item1,cart);
        assertTrue(cart.getItems().stream().count()==2);
        cartManager.clearCart(cart);
        assertTrue(cart.getItems().isEmpty());
    }
}
