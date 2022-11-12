package fr.unice.polytech.services;


import fr.unice.polytech.client.NotificationMessage;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import lombok.SneakyThrows;

import java.util.Timer;
import java.util.TimerTask;

public class StatusScheduler {

    private static StatusScheduler INSTANCE = null;
    private Timer time = new Timer();

    public static StatusScheduler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StatusScheduler();
        }
        return INSTANCE;
    }

    private StatusScheduler(Timer time) {
        this.time = time;
    }

    private StatusScheduler() {
    }

    public static StatusScheduler getInstance(Timer time ) {
        if (INSTANCE == null) {
            INSTANCE = new StatusScheduler(time);
        }
        return INSTANCE;
    }



    public void statusSchedulerTask(Order order, String clientPhoneNumber) {
        SMSService.getInstance().notifyClient(clientPhoneNumber, NotificationMessage.COMMAND_READY.getMessage());
        TimerTask fiveMinutesTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
                    SMSService.getInstance().notifyClient(clientPhoneNumber, NotificationMessage.COMMAND_READY_5_MIN.getMessage());
                } else {
                    time.cancel();
                }
            }
        };
        time.schedule(fiveMinutesTimerTask, 5 * 60 * 1000);

        TimerTask oneHourTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
                    SMSService.getInstance().notifyClient(clientPhoneNumber, NotificationMessage.COMMAND_READY_1_HOUR.getMessage());
                } else {
                    time.cancel();
                }
            }
        };
        time.schedule(oneHourTimerTask, 3600 * 1000);

        TimerTask twoHoursTimerTask = new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
                    SMSService.getInstance().notifyClient(clientPhoneNumber, NotificationMessage.COMMAND_OBSOLETE.getMessage());
                    order.setStatus(OrderStatus.OBSOLETE);
                } else {
                    time.cancel();
                }
            }
        };
        time.schedule(twoHoursTimerTask, 2 * 3600 * 1000);

    }


}
