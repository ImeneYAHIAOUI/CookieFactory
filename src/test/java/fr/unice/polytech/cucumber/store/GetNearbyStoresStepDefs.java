package fr.unice.polytech.cucumber.store;

import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.interfaces.ILocationService;
import fr.unice.polytech.interfaces.StoreFinder;
import fr.unice.polytech.interfaces.StoreRegistration;
import fr.unice.polytech.services.LocationService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;



public class GetNearbyStoresStepDefs {



    List<Store> nearbyStores;


    String location;


    List<Store> stores = new ArrayList<>();


    @Autowired
    StoreFinder storeFinder;
    @Autowired
    StoreRegistration storeRegistration;
    @Autowired
    ILocationService locationService;


    @Given("^stores with locations$")
    public void givenTheFollowingStores(List<String> addresses) {
        List<Store> existingStores = storeFinder.getStores();
        for (Store s : existingStores) {
            storeRegistration.removeStore(s);
        }
        for (String address : addresses) {
            Store store = mock(Store.class);
            when(store.getId()).thenReturn(UUID.randomUUID());
            when(store.getAddress()).thenReturn(address);
            storeRegistration.addStore(store);

            stores.add(store);
        }

    }

    @And("client located at {string}")
    public void andTheClientIsLocatedAt(String address) throws IOException {
        location = address;

        for (int i = 1; i < stores.size(); i++) {
            if (i < 3) {

                when(locationService.distance(location, stores.get(i).getAddress())).thenReturn(0.75);
                when(locationService.distance(location, stores.get(i).getAddress(), "km")).thenReturn(0.75);
                when(locationService.distance(location, stores.get(i).getAddress(), "m")).thenReturn(750.0);
            } else if (i < 4) {
                when(locationService.distance(location, stores.get(i).getAddress())).thenReturn(0.78);
                when(locationService.distance(location, stores.get(i).getAddress(), "km")).thenReturn(0.78);
                when(locationService.distance(location, stores.get(i).getAddress(), "m")).thenReturn(780.0);
            } else {
                when(locationService.distance(location, stores.get(i).getAddress())).thenReturn(5.0);
                when(locationService.distance(location, stores.get(i).getAddress(), "km")).thenReturn(5.0);
                when(locationService.distance(location, stores.get(i).getAddress(), "m")).thenReturn(5000.0);
            }
        }
    }

    @When("The client wants to retrieve nearby stores")
    public void whenTheClientWantsToRetrieveNearbyStores() throws IOException {
        nearbyStores = storeFinder.getNearbyStores(location);
    }

    @When("The client wants to retrieve stores {int} {string} away")
    public void whenTheClientWantsToRetrieveStoresADistanceAway(int distance, String unit) throws IOException {
        nearbyStores = storeFinder.getNearbyStores(location, distance, unit);
    }

    @Then("^The client should receive the stores with these locations$")
    public void thenTheClientShouldReceiveTheStoresWithTheseLoc0ations(List<String> addresses) {
        List<String> nearbyStoreAddresses = new ArrayList<>();
    for (Store s : nearbyStores) {
        nearbyStoreAddresses.add(s.getAddress());
        }
        for (String address : addresses) {

            assertTrue(nearbyStores.stream().anyMatch(store -> store.getAddress().equals(address)));
        }
    }

    @And(("^not these locations$"))
    public void andNotTheseLocations(List<String> addresses) {
        for (String address : addresses) {
            assertTrue(nearbyStores.stream().noneMatch(store -> store.getAddress().equals(address)));
        }
    }

}
