  # Created by Floriane PARIS at 11/13/2022
Feature: choose a cookie and an amount

  Scenario: Empty beginning
    Given cod with store and recipe
    And a recipe with dough "chocolate", flavour "vanilla" and toppings "top1" and "top2"
    Then Ingredient "chocolate" not in store

  Scenario: Fill inventory
    Given cod with store and recipe
    And a recipe with dough "chocolate", flavour "vanilla" and toppings "top1" and "top2"
    When add Inventory ingredient "chocolate" in amount 10
    Then Ingredient "chocolate" in store
    And Ingredient "vanilla" not in store
    And Ingredient "chocolate" in store in amount 10

  Scenario: Decrease inventory after a command
    Given cod with store and recipe
    And a recipe with dough "chocolate", flavour "vanilla" and toppings "top1" and "top2"
    And add Inventory ingredient "chocolate" in amount 10
    And add Inventory ingredient "vanilla" in amount 10
    And add Inventory ingredient "top1" in amount 2
    And add Inventory ingredient "top2" in amount 1
    When someone command a recipe with 1 cookie
    Then Ingredient "chocolate" in store in amount 9
    And Ingredient "vanilla" in store in amount 9
    And Ingredient "top1" in store in amount 1
    And Ingredient "top2" in store in amount 0

  Scenario: Decrease inventory after a command with exception
    Given cod with store and recipe
    And a recipe with dough "chocolate", flavour "vanilla" and toppings "top1" and "top2"
    And add Inventory ingredient "chocolate" in amount 10
    And add Inventory ingredient "vanilla" in amount 10
    And add Inventory ingredient "top1" in amount 2
    Then Exception if we try to command because the store doesn't have enough ingredients

  Scenario: Add in inventory after a cancelled order
    Given cod with store and recipe
    And a recipe with dough "chocolate", flavour "vanilla" and toppings "top1" and "top2"
    And add Inventory ingredient "chocolate" in amount 10
    And add Inventory ingredient "vanilla" in amount 10
    And add Inventory ingredient "top1" in amount 2
    And add Inventory ingredient "top2" in amount 1
    When someone command a recipe with 1 cookie
    And someone cancel the order with 1 cookie
    Then Ingredient "chocolate" in store in amount 10
    And Ingredient "vanilla" in store in amount 10
    And Ingredient "top1" in store in amount 2
    And Ingredient "top2" in store in amount 1

