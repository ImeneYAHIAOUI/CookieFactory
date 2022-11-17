# Created by Pauline DEVICTOR at 20/10/2022
Feature: Manage Inventory in a store

  Background:
    Given a store with address "1 rue de la Paix"
    And a cod with the store

  Scenario Outline: Add a new product as a Store manager
    Given A new Ingredient with name <name>
    And A price of <price>
    And a quantity of <quantity>
    When As a Store Manager I can add the new product in the store
    Then The new product is in the inventory's store
    Examples:
      | name        | price | quantity |
      | "Chocolate" | 2.5   | 10       |
      | "Milk"      | 1.5   | 20       |
      | "Vanilla"   | 4     | 5        |



  Scenario Outline: Add a new product as a Store manager BUT already in inventory
    Given A new Ingredient with name <name>
    And A price of <price>
    And a quantity of <quantity>
    When As a Store Manager I add a product that already exist in the store
    Then The amount is added in the inventory and is now <finalQuantity>

    Examples:
      | name        | price | quantity | finalQuantity      |
      | "Chocolate" | 2.5   | 10       |  20                |
      | "Milk"      | 1.5   | 20       |  40                |
      | "Vanilla"   | 4     | 5        |  10                |

  Scenario Outline: Subtract quantity under 0
    Given A new Ingredient with name <name>
    And A price of <price>
    And a quantity of <quantity>
    And a quantity to subtract of <quantityToSubtract>
    When As a Store Manager I subtract that quantity to the product
    Then An Error appears because I can't have negative quantity

  Examples:
    | name        | price | quantity | quantityToSubtract |
    | "Chocolate" | 2.5   | 10       |  20                |
    | "Milk"      | 1.5   | 20       |  21                |
    | "Vanilla"   | 4     | 5        |   6                |

  Scenario Outline: Subtract quantity over 0
    Given A new Ingredient with name <name>
    And A price of <price>
    And a quantity of <quantity>
    And a quantity to subtract of <quantityToSubtract>
    When As a Store Manager I subtract that quantity to the product
    Then No error appears because I don't have negative quantity

    Examples:
      | name        | price | quantity | quantityToSubtract |
      | "Chocolate" | 2.5   | 10       |   9                |
      | "Milk"      | 1.5   | 20       |  11                |
      | "Vanilla"   | 4     | 5        |   5                |

