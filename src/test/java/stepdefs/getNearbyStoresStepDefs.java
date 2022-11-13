package stepdefs;

import fr.unice.polytech.COD;
import fr.unice.polytech.services.LocationService;
import fr.unice.polytech.store.Store;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class getNearbyStoresStepDefs {


    List<Store> nearbyStores;

    COD cod;

    String location;

    LocationService locationService;

    @Given("^stores with locations$")
    public void givenTheFollowingStores(List<String> addresses) {
        cod = new COD();
        cod.initializationCod();
        for (String address : addresses) {
            Store store = mock(Store.class);
            when(store.getAddress()).thenReturn(address);
            cod.addStore(store);
        }

    }

    @And("client located at {string}")
    public void andTheClientIsLocatedAt(String address) throws IOException {
        locationService = mock(LocationService.class);
        location = address;
        for(int i=1;i<cod.getStores().size();i++){
            if(i <3 ) {
                when(locationService.distance(location,cod.getStores().get(i).getAddress())).thenReturn(0.75);
                when(locationService.distance(location,cod.getStores().get(i).getAddress(),"km")).thenReturn(0.75);
                when(locationService.distance(location,cod.getStores().get(i).getAddress(),"m")).thenReturn(750.0);
            }
            else if(i<5){
                when(locationService.distance(location,cod.getStores().get(i).getAddress())).thenReturn(0.78);
                when(locationService.distance(location,cod.getStores().get(i).getAddress(),"km")).thenReturn(0.78);
                when(locationService.distance(location,cod.getStores().get(i).getAddress(),"m")).thenReturn(780.0);
            }
            else {
                when(locationService.distance(location,cod.getStores().get(i).getAddress())).thenReturn(5.0);
                when(locationService.distance(location,cod.getStores().get(i).getAddress(),"km")).thenReturn(5.0);
                when(locationService.distance(location,cod.getStores().get(i).getAddress(),"m")).thenReturn(5000.0);
            }
       }
        cod.setLocationService(locationService);
    }

    @When("The client wants to retrieve nearby stores")
    public void whenTheClientWantsToRetrieveNearbyStores() throws IOException {
        nearbyStores = cod.getNearbyStores(location);
    }

    @When("The client wants to retrieve stores {int} {string} away")
    public void whenTheClientWantsToRetrieveStoresADistanceAway(int distance, String unit) throws IOException {
        nearbyStores = cod.getNearbyStores(location, distance, unit);
    }

    @Then("^The client should receive the stores with these locations$")
    public void thenTheClientShouldReceiveTheStoresWithTheseLoc0ations(List<String> addresses) {
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
