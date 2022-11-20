# Created by imene at 11/20/2022
Feature: TimeSlot Attribution for a date

  Background:
    Given a cook with an empty timetable
    And a random recipe with cooking time of 60
    And a store with the recipe, the cook opening time "08:00" and ending time "10:00"

  Scenario: Cook can do the order
    When a cart with 2 of the recipe for the date "20/12/2022"
    Then The cook can do the order

  Scenario: Cook can't do the order
    Given a cook with a timetable with a timeTable occupied from "08:00" to "10:00" on the date "20/12/2022"
    When a cart with 2 of the recipe for the date "20/12/2022"
    Then The cook can't do the order

  Scenario: Cancel order
    When an order with 1 of the recipe for the date "20/12/2022"
    Then The cook can do the order
    And add order does not throw exception
    And cancel order
    Then cook has exactly 0 time slot