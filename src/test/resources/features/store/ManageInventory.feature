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
    Then An Error appears because it's already in the inventory
    Examples:
      | name        | price | quantity |
      | "Chocolate" | 2.5   | 10       |
      | "Milk"      | 1.5   | 20       |
      | "Vanilla"   | 4     | 5        |
