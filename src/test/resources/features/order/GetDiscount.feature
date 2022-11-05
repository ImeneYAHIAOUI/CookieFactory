Feature: Make order and get discount

  Background:
    Given a registered client

  Scenario:
    When the client makes an order of 1 cookies
    And the client makes a second order of 29 cookies
    Then the client has a discount of 10%

  Scenario:
    When the client makes an order of 10 cookies
    And the client makes a second order of 30 cookies
    Then the client has a discount of 10%

  Scenario:
    When the client makes an order of 29 cookies
    And the client makes a second order of 1 cookies
    Then the client has a discount of 10%

  Scenario:
    When the client makes an order of 1 cookies
    And the client makes a second order of 1 cookies
    Then the client does not have a discount

  Scenario:
    When the client makes an order of 1 cookies
    And the client makes a second order of 5 cookies
    Then the client does not have a discount

  Scenario:
    When the client makes an order of 1 cookies
    And the client makes a second order of 28 cookies
    Then the client does not have a discount

  Scenario:
    When the client makes an order of 30 cookies
    And the client makes a second order of 2 cookies
    Then the client does not have a discount

  Scenario:
    When the client makes an order of 30 cookies
    And the client makes a second order of 30 cookies
    Then the client does not have a discount

  Scenario:
    When the client makes an order of 100 cookies
    And the client makes a second order of 1 cookies
    Then the client does not have a discount

  Scenario:
    When the client makes an order of 100 cookies
    And the client makes a second order of 30 cookies
    Then the client does not have a discount

