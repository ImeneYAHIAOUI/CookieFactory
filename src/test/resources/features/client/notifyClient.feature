# Created by imene at 11/11/2022
Feature: notify client
  # Enter feature description here

  Background: # Enter background description here
    Given a client with an order ready for pickup

  @notify-client
  Scenario: client picks up command
    When the client picks up the order
    Then the client gets notified and doesn't receive more notifications

  @notify-client
  Scenario: client never picks up the order
    When the client never picks up the order
    Then the client gets notified and the order is obsolete

