# Created by Sourour GAZZEH at 17/10/2022
Feature: Cook suggest recipe

  Background:
    Given a cook with id 1005
    And a cod

    Scenario: Suggest recipe name "Iced Cookie"
      When cook create Cookie with name "Iced Cookie"
      Then suggested recipe is added to suggested recipe list

    Scenario: Cookie with  name "Iced Cookie" is accepted
      When COD accept recipe
      Then suggested recipe is added to recipe list
      And suggested recipe is removed from suggested recipe list

    Scenario: Cookie  name "Iced Cookie" is declined
      When COD decline recipe
      Then suggested recipe is removed from suggested recipe list
