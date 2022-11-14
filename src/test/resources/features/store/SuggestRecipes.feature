  # Created by Floriane PARIS at 11/14/2022
Feature: Suggest a recipe

  Scenario: Suggest recipe
    Given empty cod
    When add the ingredient with name "dough", price 4.4 and type "DOUGH"
    And add the ingredient with name "flavour", price 4.4 and type "FLAVOUR"
    And add the ingredient with name "topping", price 4.4 and type "TOPPING"
    Then suggest Recipe with Dough "dough", Flavour "flavour" and Topping "topping" without exception

  Scenario: Suggest recipe with wrong type
    Given empty cod
    When add the ingredient with name "dough", price 4.4 and type "DOUGH"
    And add the ingredient with name "flavour", price 4.4 and type "FLAVOUR"
    And add the ingredient with name "topping", price 4.4 and type "TOPPING"
    Then suggest Recipe with Dough "topping", Flavour "flavour" and Topping "topping" with exception
    And suggest Recipe with Dough "dough", Flavour "f" and Topping "topping" with exception
    And suggest Recipe with Dough "dough", Flavour "topping" and Topping "flavour" with exception