package fr.unice.polytech.client;

import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegisteredClient extends Client {

    private final static int LOYALTY = 30;
    @Getter
    private final String id;
    @Getter
    private final String password;
    @Setter
    @Getter
    private List<Order> pastOrders;
    private int nbCookie;
    @Getter
    private String remainingBanTime;
    @Getter
    private boolean eligibleForDiscount;
    @Getter
    private boolean toGoodToGoClient;
    @Getter
    private String mail;
    @Getter
    private List<LocalDateTime> notificationsDates;

    public RegisteredClient(String id, String password, String phoneNumber) throws InvalidPhoneNumberException {
        super(phoneNumber);
        this.id = id;
        this.password = password;
        nbCookie = 0;
        this.pastOrders = new ArrayList<>();
        eligibleForDiscount = false;
        toGoodToGoClient = false;
        mail = null;
        notificationsDates = new ArrayList<>();
    }

    @Override
    public void addToGoodToGo(String mail, List<LocalDateTime> list){
        toGoodToGoClient = true;
        this.mail = mail;
        this.notificationsDates = List.copyOf(list);
    }

    @Override
    public void validateOrder(Order order) {
        int nb = 0;
        for (Item item : this.getCart().getItems()) {
            nb += item.getQuantity();
        }
        eligibleForDiscount = nbCookie < LOYALTY && nbCookie + nb >= LOYALTY;
        nbCookie += nb;
        pastOrders.add(order);
        getCart().emptyItems();
    }

    @Override
    public boolean isRegistered(){
        return true;
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

        return calculateTimeBetweenTwoLastOrders(lastOrder, beforeLastOrder);
    }

    /**
     * Calculate the time between the two last cancelled orders
     * @param lastOrder the last order cancelled
     * @param beforeLastOrder the order cancelled before the last one
     * @return true if the time between the two last orders is less than 8 minutes
     */
    private boolean calculateTimeBetweenTwoLastOrders(Order lastOrder, Order beforeLastOrder) {
        Date lastOrderDate = lastOrder.getHistory().get(OrderStatus.CANCELLED);
        Date beforeLastOrderDate = beforeLastOrder.getHistory().get(OrderStatus.CANCELLED);
        long differenceInMillies = Math.abs(lastOrderDate.getTime() - beforeLastOrderDate.getTime());
        long differenceInMinutes = (differenceInMillies/1000) / 60 ;

        long remainingBanTimeInmilis = Math.abs(System.currentTimeMillis() - lastOrderDate.getTime());
        this.remainingBanTime = String.format("%02d minutes and %02d seconds",
                (remainingBanTimeInmilis / 1000) / 60, (remainingBanTimeInmilis) / 1000 % 60);

        return differenceInMinutes < 8;
    }


}
