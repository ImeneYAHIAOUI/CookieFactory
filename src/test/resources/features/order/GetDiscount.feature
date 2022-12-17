Feature: Make order and get discount

  Background:
    Given a registered client

  Scenario: Order 1 then 29 cookies
    When the client makes an order of 1 cookies
    And the client makes a second order of 29 cookies
    Then the client has a discount of 10%

  Scenario: Order 10 then 30 cookies
    When the client makes an order of 10 cookies
    And the client makes a second order of 30 cookies
    Then the client has a discount of 10%

  Scenario: Order 29 then 1 cookies
    When the client makes an order of 29 cookies
    And the client makes a second order of 1 cookies
    Then the client has a discount of 10%

  Scenario: Order 1 then 1 cookies
    When the client makes an order of 1 cookies
    And the client makes a second order of 1 cookies
    Then the client does not have a discount

  Scenario: Order 1 then 5 cookies
    When the client makes an order of 1 cookies
    And the client makes a second order of 5 cookies
    Then the client does not have a discount

  Scenario: Order 1 then 28 cookies
    When the client makes an order of 1 cookies
    And the client makes a second order of 28 cookies
    Then the client does not have a discount

  Scenario: Order 30 then 2 cookies
    When the client makes an order of 30 cookies
    And the client makes a second order of 2 cookies
    Then the client does not have a discount

  Scenario: Order 30 then 30 cookies
    When the client makes an order of 30 cookies
    And the client makes a second order of 30 cookies
    Then the client does not have a discount

  Scenario: order 100 then 1 cookies
    When the client makes an order of 100 cookies
    And the client makes a second order of 1 cookies
    Then the client does not have a discount

  Scenario: Order 100 then 30 cookies
    When the client makes an order of 100 cookies
    And the client makes a second order of 30 cookies
    Then the client does not have a discount

