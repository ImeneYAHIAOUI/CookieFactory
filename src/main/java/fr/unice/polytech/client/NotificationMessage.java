package fr.unice.polytech.client;

import lombok.Getter;

/**
 * Enum class to store all the different messages that can be sent to the client
 */
public enum NotificationMessage {
    COMMAND_READY("Your order is ready, please come and pick it up."),
    COMMAND_READY_5_MIN("Your has been ready for the last 5 minutes, please come and pick it up."),
    COMMAND_READY_1_HOUR("Your has been ready for the last hour, please come and pick it up."),
    COMMAND_OBSOLETE("Your has been ready for the last 2 hours. unfortunately, you can no longer retrieve it.");
    @Getter
    private final String message;

    NotificationMessage(String message) {
        this.message = message;
    }
}
