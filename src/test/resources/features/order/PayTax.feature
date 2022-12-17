# Created by sourour
Feature: Pay tax

  Scenario: unregistered client pay tax when make order
    Given a client not registered in base
    And a store with tax 15.0
    When the client choose a cookie with price 8.0
    Then the client pay 9.2

  Scenario: registered client pay tax when make order
    Given a registered client in base
    And a store with tax 15.0
    When the client choose a cookie with price 8
    Then the client pay 9.2