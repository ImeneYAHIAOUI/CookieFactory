# Created by Pauline DEVICTOR at 17/10/2022
Feature: Change Opening Hours

  Background:

    Given a store with address "1 rue de la Paix"
    And a cod with the store

  Scenario Outline: Change Opening Hours to <NewOpeningTime> and <NewClosingTime>
    Given NewOpeningTime with time <NewOpeningTime>
    And NewClosingTime with time <NewClosingTime>
    When The manager set on COD the store hours to the new ones
    Then The store hours are changed to the new ones
    Examples:
      | NewOpeningTime   | NewClosingTime  |
      | "10:00"          | "21:00"         |
      | "12:00"          | "23:00"         |
      | "20:00"          | "01:00"         |
      | "00:00"          | "23:59"         |