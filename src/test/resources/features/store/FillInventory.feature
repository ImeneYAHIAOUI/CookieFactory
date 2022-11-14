  # Created by Floriane PARIS at 11/13/2022
Feature: choose a cookie and an amount

  Scenario: Empty beginning
    Given cod with store and recipe
    Then Ingredient "chocolate" not in store

  Scenario: Fill inventory
    Given cod with store and recipe
    When add Inventory ingredient "chocolate" in amount 10
    Then Ingredient "chocolate" in store
    And Ingredient "chocolat" not in store
    And Ingredient "chocolate" in store in amount 10