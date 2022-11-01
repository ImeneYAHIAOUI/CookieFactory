# Created by Pauline DEVICTOR at 01/11/2022

Feature: Pay an Order

  Background:
    Given an empty cod and a unregistered client with phone number 06
    And the client's cart contains 1 cookies of type "Chocolala"

  Scenario: As a Client Pay an Order (no problems)
    When I confirm my cart and pay my order
    Then I should see a confirm message and my orderID is 0

  Scenario: As a Client Pay an Order (already orders in the list)
    Given the cod already has some orders
    When I confirm my cart and pay my order
    Then I should see a confirm message and get my orderID

