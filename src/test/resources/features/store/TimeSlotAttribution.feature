# Created by Floriane PARIS at 01/11/2022
Feature: TimeSlot Attribution

  Background:
    Given a cook with an empty timetable
    And a random recipe with cooking time of 60
    And a store with the recipe, the cook opening time "08:00" and ending time "10:00"

  Scenario: Cook can do the order
    When a cart with 1 of the recipe
    Then The cook can do the order

  Scenario: Cancel order
    When an order with 1 of the recipe
    And add order does not throw exception
    And cancel order
    Then cook has exactly 0 time slot



