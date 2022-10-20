Feature: Order a cookie

  Background:
    Given A client with phone number 12345678
    And a cod to stock data
    And a Cookie "Chocolala"

  Scenario: Choose amount
    When Client add amount 3 of cookie to cart
    Then Cart is not empty
    And Item is added to cart
    And Right quantity 3 is added to cart

  Scenario: Client finalize order
    When Client add amount 1 of cookie to cart
    And Client finalize order
    Then Cod orders is not empty
    And Order with right client is added to Cod
    And Order with right item is added to Cod
    And Cart is empty
