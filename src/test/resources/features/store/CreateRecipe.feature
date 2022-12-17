# Created by Sourour GAZZEH at 17/10/2022
Feature: Cook suggest recipe

  Background:
    Given a cook
    And a cookie with name "Iced Cookie"

    Scenario: Suggest recipe with name "Iced Cookie"
      When cook suggest recipe
      Then suggested recipe is added to suggested recipe list

    Scenario: Cookie with  name "Iced Cookie" is accepted
      When COD accept recipe
      Then suggested recipe is added to recipe list
      And suggested recipe is removed from suggested recipe list

    Scenario: Cookie  name "Iced Cookie" is declined
      When COD decline recipe
