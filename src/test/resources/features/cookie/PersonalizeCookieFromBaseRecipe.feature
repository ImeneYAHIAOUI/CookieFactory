# Created by imene at 11/22/2022
Feature: personalize party cookie
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

    Scenario: Client want to personalize a cookie with unavailable ingredients
      Given the store has the required occasion
      And the store has a cook specialised in the chosen theme
      And the chosen cookie
      When the client wants to personalize the cookie of by adding
      |m&ms |
      |caramel|
      And removing
      |chocolate chips|
      And order 1 of them of size "L"
      Then the cookie cannot be ordered because the chosen cookie is not available or because there isn't enough ingredients

  Scenario: Client want to personalize a cookie with unavailable amounts of ingredients
    Given the store has the required occasion
    And the store has a cook specialised in the chosen theme
    And the chosen cookie
    When the client wants to personalize the cookie of by adding
      |chocolate dough  |
      |white chocolate chips|
    And removing
      |chocolate chips|
    And order 5 of them of size "XL"
    Then the cookie cannot be ordered because the chosen cookie is not available or because there isn't enough ingredients


  Scenario: Client want to personalize a an unavailable cookie
    Given the store has the required occasion
    And the store has a cook specialised in the chosen theme
    When the client wants to personalize the cookie of by adding
      |chocolate dough  |
      |white chocolate chips|
    And removing
      |chocolate chips|
    And order 1 of them of size "L"
    Then the cookie cannot be ordered because the chosen cookie is not available or because there isn't enough ingredients

  Scenario: Client want to personalize a cookie with unavailable themes
    Given the store has the required occasion
    And the chosen cookie
    When the client wants to personalize the cookie of by adding
      |chocolate dough  |
      |white chocolate chips|
    And removing
      |chocolate chips|
    And order 1 of them of size "L"
    Then the cookie cannot be ordered because the chosen theme and or occasion are not available

  Scenario: Client want to personalize a cookie with an unavailable theme
    Given the store has a cook specialised in the chosen theme
    And the chosen cookie
    When the client wants to personalize the cookie of by adding
      |chocolate dough  |
      |white chocolate chips|
    And removing
      |chocolate chips|
    And order 1 of them of size "L"
    Then the cookie cannot be ordered because the chosen theme and or occasion are not available

  Scenario: Client want to personalize a cookie with an unavailable theme
    Given the store has the required occasion
    And the store has a cook specialised in the chosen theme
    And the chosen cookie
    When the client wants to personalize the cookie of by adding
      |chocolate dough  |
      |white chocolate chips|
    And removing
      |chocolate chips|
    And order 1 of them of size "XXL"
    Then the cookie personalized from base recipe can be ordered
    And the cookie's price is changed



