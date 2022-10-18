# Created by Pauline DEVICTOR at 17/10/2022
Feature: Change Opening Hours

  Background:

    Given a store with address "1 rue de la Paix"
    And a cod with the store
    And NewOpeningTime with time "10:00"
    And NewClosingTime with time "21:00"

  Scenario: Change Opening Hours to "10:00" to "21:00"
    When The manager set on COD the store hours to the new ones
    Then The store hours are changed to the new ones
