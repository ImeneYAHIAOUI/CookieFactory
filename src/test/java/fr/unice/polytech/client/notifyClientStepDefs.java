package fr.unice.polytech.client;

import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.services.SMSService;
import fr.unice.polytech.services.StatusScheduler;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class notifyClientStepDefs {

    @Spy
    Timer mockedTimer = spy(new Timer());
    Timer timer = new Timer();
    Client client;
    @Spy
    Order order ;



    @Given("a client with phone number {string}")
    public void aClientWithPhoneNumber(String phoneNumber) {
        client = new UnregisteredClient(phoneNumber);


    }


    @And("the order is ready")
    public void anOrderIsReady() throws OrderException {
        LocalTime openingTime = LocalTime.of(10, 0);
        LocalTime closingTime = LocalTime.of(20, 0);
        LocalTime pickupTime = LocalTime.of(12, 0);
        client.getCart().setPickupTime(pickupTime);
        Order order2 = new Order("1",client,new Cook(1),new Store(new ArrayList<>(), new ArrayList<>(), "address",openingTime,closingTime,1,new Inventory(new ArrayList<>()),0.2,new ArrayList<>()));
        order = spy(order2);
        doAnswer(invocation -> {
            when(order.getStatus()).thenReturn(invocation.getArgument(0));
            return null;
        }).when(order).setStatus(any(OrderStatus.class));

        doAnswer( invocation -> {
            timer.schedule(invocation.getArgument(0), 0);
            return null;
        }).when(mockedTimer).schedule(any(TimerTask.class), anyLong());
        doAnswer( invocation -> {
            timer.cancel();
            timer = new Timer();
            return null;
        }).when(mockedTimer).cancel();



    }

    @Then("the client gets notified and doesn't receive more notifications")
    public void theClientShouldBeNotified() throws InterruptedException {
        StatusScheduler.getInstance(mockedTimer).statusSchedulerTask(order, client.getPhoneNumber());
        verify(mockedTimer, times(3)).schedule(any(TimerTask.class), anyLong());
    }


    @When("the client never picks up the order")
    public void theClientDoesntPickUpTheOrder() {
        when(order.getStatus()).thenReturn(OrderStatus.READY);

    }
    @Then("the client gets notified and the order is obsolete")
    public void theClientGetsNotifiedAndTheOrderIsObsolete() throws InterruptedException {
        StatusScheduler.getInstance(mockedTimer).statusSchedulerTask(order, client.getPhoneNumber());
        Thread.sleep(10);
        assertEquals(OrderStatus.OBSOLETE,order.getStatus());
    }


    @When("the client picks up the order")
    public void theClientPicksUpOrderOneHourAfter(){
        when(order.getStatus()).thenReturn(OrderStatus.READY,OrderStatus.READY,OrderStatus.COMPLETED);
    }




}
