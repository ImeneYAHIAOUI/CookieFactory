package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.ClientException;
import fr.unice.polytech.exception.InvalidInputException;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.RegistrationException;
import fr.unice.polytech.interfaces.ClientFinder;
import fr.unice.polytech.interfaces.ClientHandler;
import fr.unice.polytech.repositories.ClientRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

@Component
public class ClientManager implements ClientHandler, ClientFinder
{
    @Getter
    private final ClientRepository clients;
    @Getter
    private final List<RegisteredClient> registeredClients;
    @Getter
    private final List<RegisteredClient> connectedClients;
    @Getter
    private final List<RegisteredClient> tooGoodToGoClients;
    @Autowired
    public ClientManager(ClientRepository clients){
        this.clients=clients;
        registeredClients =new ArrayList<>();
        connectedClients=new ArrayList<>();
        tooGoodToGoClients =new ArrayList<>();
        clients.findAll().forEach(client ->{
            if(client instanceof  RegisteredClient){
                registeredClients.add((RegisteredClient) client);
                if(((RegisteredClient) client).isToGoodToGoClient()){
                    tooGoodToGoClients.add((RegisteredClient) client);
                }
            }
        });
    }
    @Override
    public boolean isBanned(RegisteredClient client)  {
        List<Order> cancelledOrders = client.getPastOrders().stream().filter(order ->
                order.getState().getOrderStatus() == OrderStatus.CANCELLED).filter(order ->
                order.getHistory().get( OrderStatus.CANCELLED).getTime() >= System.currentTimeMillis() - 1000*10*60).toList();

        if(cancelledOrders.size() < 2) {
            return false;
        }
        Order lastOrder = cancelledOrders.get(cancelledOrders.size() - 1);
        Order beforeLastOrder = cancelledOrders.get(cancelledOrders.size() - 2);

        return calculateTimeBetweenTwoLastOrders(client,lastOrder, beforeLastOrder);
    }
    /**
     * Calculate the time between the two last cancelled orders
     * @param lastOrder the last order cancelled
     * @param beforeLastOrder the order cancelled before the last one
     * @return true if the time between the two last orders is less than 8 minutes
     */

    public boolean calculateTimeBetweenTwoLastOrders(RegisteredClient client,Order lastOrder, Order beforeLastOrder) {
        Date lastOrderDate = lastOrder.getHistory().get(OrderStatus.CANCELLED);
        Date beforeLastOrderDate = beforeLastOrder.getHistory().get(OrderStatus.CANCELLED);
        long differenceInMillies = Math.abs(lastOrderDate.getTime() - beforeLastOrderDate.getTime());
        long differenceInMinutes = (differenceInMillies/1000) / 60 ;

        long remainingBanTimeInmilis = Math.abs(System.currentTimeMillis() - lastOrderDate.getTime());
        client.setRemainingBanTime(String.format("%02d minutes and %02d seconds",
                (remainingBanTimeInmilis / 1000) / 60, (remainingBanTimeInmilis) / 1000 % 60));

        return differenceInMinutes < 8;
    }

    /**
     * Check if the phone number is valid
     *
     * @param phoneNumber the phone number to check
     * @throws InvalidPhoneNumberException if the phone number is invalid
     */
    @Override
    public void checkPhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        if(phoneNumber==null){
            throw new InvalidPhoneNumberException(phoneNumber);
        }
        Pattern pattern = Pattern.compile("(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new InvalidPhoneNumberException(phoneNumber);
        }
    }

    @Override
    public void addToGoodToGo(Client client,String mail, List<LocalDateTime> list, Store store)throws ClientException{

        if(client  instanceof RegisteredClient){
            ((RegisteredClient) client).setToGoodToGoClient(true);
            ((RegisteredClient) client).setMail(mail);
            ((RegisteredClient) client).setNotificationsDates(list);
            this.tooGoodToGoClients.add((RegisteredClient)client);
        }else{
            throw new ClientException("You can't have notifications for the Too Good To Go bags of you are not registered.");
        }

    }

    /**
     * Register a new Client
     * @param mail  chosen by the client
     * @param password chosen by the client
     * @param phoneNumber chosen by the client
     * @throws InvalidPhoneNumberException if phone number invalid
     */

    @Override
    public void register( String mail,String password, String phoneNumber) throws RegistrationException,InvalidPhoneNumberException {
        Optional<Client> cl=StreamSupport.stream(clients.findAll().spliterator(), false)
                .filter(cust -> mail.equals(((RegisteredClient)cust).getMail())).findAny();
        if (cl.isPresent())
            throw new RegistrationException("User with " + mail + " is already registered.");
        UUID id;
        do{
            id = UUID.randomUUID();
        }while (clients.existsById(id));

        checkPhoneNumber(phoneNumber);
        RegisteredClient client=new RegisteredClient(password, phoneNumber,mail);
        clients.save(client,id);
        registeredClients.add(client);
    }

    @Override
    public RegisteredClient logIn(String mail, String password) throws InvalidInputException {
        if (connectedClients.stream().noneMatch(client -> client.getMail().equals(mail))) {
            Optional<RegisteredClient> registeredClient = (registeredClients.stream().filter(client -> client.getMail().equals(mail)).findFirst());
            if (registeredClient.isPresent()) {
                RegisteredClient client = registeredClient.get();
                if (client.getPassword().equals(password)){
                    connectedClients.add(client);
                    return client;
                }
                else
                    throw new InvalidInputException("The password you entered is not valid. ");

            } else
                throw new InvalidInputException("mail not found. Please log in with another mail");
        }
        throw new InvalidInputException("You are already connected ");
    }
    @Override
    public void logOff(RegisteredClient client){
        this.connectedClients.remove(client);
    }

    @Override
    public List<Order> getClientPastOrders(RegisteredClient client){
        return client.getPastOrders();
    }

    @Override
    public void clearTooGoodToGoClients(){
        tooGoodToGoClients.clear();
    }


}
