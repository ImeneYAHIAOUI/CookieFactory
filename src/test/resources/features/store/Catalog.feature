  # Created by Floriane PARIS at 11/13/2022
Feature: choose a cookie and an amount

  Scenario: Can get anything in empty catalog
    Given an empty catalog
    Then get the ingredient with name "chocolate" throws exception

  Scenario: Can get a ingredient added to the catalog
    Given an empty catalog
    When add the ingredient with name "chocolate", price 4.4 and type "DOUGH" does not throw exception
    Then get the ingredient with name "chocolate" does not throw exception

  Scenario: Can't add twice the same ingredient
    Given an empty catalog
    When add the ingredient with name "vanilla", price 4.4 and type "DOUGH" does not throw exception
    Then add the ingredient with name "vanilla", price 4.5 and type "FLAVOUR" throws exception

  Scenario: Can add multiple ingredients with different name
    Given an empty catalog
    When add the ingredient with name "chocolate_dough", price 4.4 and type "DOUGH" does not throw exception
    Then add the ingredient with name "chocolate_flavour", price 4.4 and type "DOUGH" does not throw exception
    And add the ingredient with name "chocolate_topping", price 1 and type "TOPPING" does not throw exception

  Scenario: Can get multiple times the same ingredient
    Given an empty catalog
    When add the ingredient with name "caramel", price 4.4 and type "DOUGH" does not throw exception
    Then get the ingredient with name "caramel" does not throw exception
    And get the ingredient with name "caramel" does not throw exception