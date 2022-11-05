# Created by imene at 11/1/2022
Feature: Retrieve past orders for registered clients
  # Enter feature description here

  Background:
    Given a Registered client with id "1" and phone number "0606060606"

    # Enter steps here
  Scenario: get past orders
    When a the Client has theses past orders
      | chocolala 3 | strawberry 2 | m&ms 4 |
      | maxiChoco 5 | chocolala 2 | vanilla 3 |
      | sneakers 2 | caramel 1 | raspberry 2 |
    And They want to retrieve their past orders
    Then The client should have their past orders


