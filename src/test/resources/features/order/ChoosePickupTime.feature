# Created by joel at 06/11/2022
Feature: Choose pickup time

  Scenario: Choose pickup time
    Given a client with a cart containing some items
    When the client chooses a valid pickup time
    Then the pickup time is set to the client's cart

  Scenario: Choose pickup time not in the valid pickup time list
    Given a client with a cart containing some items
    When the client chooses a pickup time that is not in the valid pickup time list
    Then the pickup time is not set to the client's cart
