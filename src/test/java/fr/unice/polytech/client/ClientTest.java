package fr.unice.polytech.client;

import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.PickupTimeNotSetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientTest {
    Client client;

    @Test
    public void createClientOK() {
        assertDoesNotThrow(() -> {
            client = new UnregisteredClient("0606060606");
        });
    }

    @Test
    public void createClientPhoneNumberWithSpaces() {
        assertDoesNotThrow(() -> {
            client = new UnregisteredClient("06 06 06 06 06");
        });
    }

    @Test
    public void createClientPhoneNumberWithDashes() {
        assertDoesNotThrow(() -> {
            client = new UnregisteredClient("06-06-06-06-06");
        });
    }

    @Test
    public void createClientPhoneNumberWithRegionCode() {
        assertDoesNotThrow(() -> {
            client = new UnregisteredClient("+33606060606");
        });
    }

    @Test
    public void createClientPhoneNumberWithSpacesAndRegionCode() {
        assertDoesNotThrow(() -> {
            client = new UnregisteredClient("+33 6 06 06 06 06");
        });
    }

    @Test
    public void createClientPhoneNumberWithDashesAndRegionCode() {
        assertDoesNotThrow(() -> {
            client = new UnregisteredClient("+33 6-06-06-06-06");
        });
    }

    @Test
    public void createClientPhoneNumberWith11Digits() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("06060606060"));
    }

    @Test
    public void createClientPhoneNumberWith9Digits() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("060606060"));
    }

    @Test
    public void createClientPhoneNumberWithLetters() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("060606060a"));
    }

    @Test
    public void createClientPhoneNumberWithSpecialCharacters() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("060606060*"));
    }

    @Test
    public void createClientPhoneNumberWithEmptyString() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient(""));
    }

    @Test
    public void createClientPhoneNumberWithNull() {
        assertThrows(NullPointerException.class, () -> client = new UnregisteredClient(null));
    }

    @Test
    public void createClientPhoneNumberWithSpacesAndNull() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("  "));
    }

    @Test
    public void createClientPhoneNumberWithDashesAndNull() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("--"));
    }

    @Test
    public void createClientPhoneNumberWithRegionCodeAndNull() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("+33"));
    }

    @Test
    public void createClientPhoneNumberWithSpacesAndRegionCodeAndNull() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("+33  "));
    }

    @Test
    public void createClientPhoneNumberWithDashesAndRegionCodeAndNull() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("+33--"));
    }

    @Test
    public void createClientPhoneNumberWithRegionCodeAnd11Digits() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("+336060606060"));
    }

    @Test
    public void createClientPhoneNumberWithRegionCodeAnd9Digits() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("+3360606060"));
    }

    @Test
    public void createClientPhoneNumberWithRegionCodeAndLetters() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("+3360606060a"));
    }

    @Test
    public void createClientPhoneNumberWithRegionCodeAndSpecialCharacters() {
        assertThrows(InvalidPhoneNumberException.class, () -> client = new UnregisteredClient("+3360606060*"));
    }

    @Test
    public void getEstimatedTimeSlotWithNoPickUpTime() throws InvalidPhoneNumberException {
        client = new UnregisteredClient("0606060606");
        assertThrows(PickupTimeNotSetException.class, () -> client.getCart().getEstimatedTimeSlot());
    }

    @Test
    public void isRegisteredCheck() throws InvalidPhoneNumberException {
        Client client1 = new UnregisteredClient("0606060606");
        Client client2 = new RegisteredClient("1","pwd","0606060606");
        assert(!client1.isRegistered());
        assert(client2.isRegistered());
    }


}
