# Created by imene at 11/25/2022
Feature: personalize party cookie from scratch
  # Enter feature description here

  Background:
    Given an inventory with the ingredients
      | chocolate chips       | 8 |
      | chocolate dough       | 10 |
      | vanillaFlavour        | 30 |
      | strawberryFlavour     | 50 |
      | chocolateFlavour      | 20 |
      | white chocolate chips | 10 |
      | m&ms                  | 0  |
      | caramel               | 0  |
    And a store
  Scenario: client choose ingredients that are not available
    Given the store has the required occasion
    And the store has a cook specialised in the chosen theme
    Then the client wants to personalize the cookie of by choosing dough "chocolate dough", flavour "strawberryFlavour"
    And adding the toppings
    | chocolate chips |
    And adding the ingredients
    | white chocolate chips |
    | m&ms |
    And order 1 of them of size "L"
    Then the cookie personalized from scratch cannot be ordered because the store does not have the required ingredients


  Scenario: client choose ingredients with amounts that are not available
    Given the store has the required occasion
    And the store has a cook specialised in the chosen theme
    Then the client wants to personalize the cookie of by choosing dough "chocolate dough", flavour "strawberryFlavour"
    And adding the toppings
      | chocolate chips |
    And adding the ingredients
      | white chocolate chips |
      | chocolate chips |
      | vanillaFlavour |
    And order 3 of them of size "L"
    Then the cookie personalized from scratch cannot be ordered because the store does not have the required ingredients

  Scenario: Client want to personalize a cookie from scratch with unavailable themes
    Given the store has the required occasion
    Then the client wants to personalize the cookie of by choosing dough "chocolate dough", flavour "strawberryFlavour"
    And adding the toppings
      | chocolate chips |
    And adding the ingredients
      | white chocolate chips |
      | chocolate chips |
      | vanillaFlavour |
    And order 1 of them of size "L"
    Then the cookie cannot be ordered because the chosen theme and or occasion are not available

  Scenario: Client want to personalize a cookie from scratch with unavailable occasion
    Given the store has a cook specialised in the chosen theme
    Then the client wants to personalize the cookie of by choosing dough "chocolate dough", flavour "strawberryFlavour"
    And adding the toppings
      | chocolate chips |
    And adding the ingredients
      | white chocolate chips |
      | chocolate chips |
      | vanillaFlavour |
    And order 1 of them of size "L"
    Then the cookie cannot be ordered because the chosen theme and or occasion are not available

    Scenario: Client want to personalize a cookie from scratch with available ingredients, occasions and themes
      Given the store has the required occasion
      And the store has a cook specialised in the chosen theme
      Then the client wants to personalize the cookie of by choosing dough "chocolate dough", flavour "strawberryFlavour"
      And adding the toppings
        | chocolate chips |
      And adding the ingredients
        | white chocolate chips |
        | chocolate chips |
        | vanillaFlavour |
      And order 2 of them of size "L"
      Then the cookie personalized from scratch can be ordered
      And the cookie's price is calculated based on the size and ingredients





