package fr.unice.polytech.client;

import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RegisteredClient extends Client {

    @Getter
    private final String id;
    @Getter
    private final String password;
    @Getter
    private List<Order> pastOrders;
    @Getter
    private int nbCookie;

    private String remainingBanTime;
    public RegisteredClient(String id, String password, int phoneNumber) {
        super(phoneNumber);
        this.id = id;
        this.password = password;
        nbCookie = 0;
        this.pastOrders = new ArrayList<>();
    }

    @Override
    public void emptyCart(Order order) {
        int nb = 0;
        for (Item item : this.getCart().getItems()) {
            nb += item.getQuantity();
        }
        nbCookie += nb;
        pastOrders.add(order);
        getCart().emptyItems();
    }


    public String getRemainingBanTime() {
        return remainingBanTime;
    }

    public boolean isBanned()
    {
        List<Order> cancelledOrders = this.pastOrders.stream().filter(order ->
                order.getStatus() == OrderStatus.CANCELLED).filter(order ->
                order.getHistory().get( OrderStatus.CANCELLED).getTime() >= System.currentTimeMillis() - 1000*10*60).toList();

        if(cancelledOrders.size() < 2) {
            return false;
        }
        Order lastOrder = cancelledOrders.get(cancelledOrders.size() - 1);
        Order beforeLastOrder = cancelledOrders.get(cancelledOrders.size() - 2);


        Date lastOrderDate = lastOrder.getHistory().get(OrderStatus.CANCELLED);
        Date beforeLastOrderDate = beforeLastOrder.getHistory().get(OrderStatus.CANCELLED);
        long differenceInMillies = Math.abs(lastOrderDate.getTime() - beforeLastOrderDate.getTime());
        long differenceInMinutes = (differenceInMillies/1000) / 60 ;

        long remainingBanTimeInmilis = Math.abs(System.currentTimeMillis() - lastOrderDate.getTime());
                this.remainingBanTime = String.format("%02d minutes and %02d seconds",
                        (remainingBanTimeInmilis / 1000) / 60, (remainingBanTimeInmilis) / 1000 % 60);

        return differenceInMinutes < 8;




    }
     public void setPastOrders(List<Order> pastOrders) {
         this.pastOrders = pastOrders;
     }

}
