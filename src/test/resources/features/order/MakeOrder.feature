# Created by joel at 17/10/2022
Feature: Make order
  Background:
    Given a client
    And an order from this client
  Scenario:
    When the client cancels the order
    Then the order has status "CANCELLED"