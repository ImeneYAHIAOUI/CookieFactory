# Created by imene at 10/17/2022
Feature: Order Status Change

  Background:
    Given an order

  Scenario: order status is "in progress" and we want to cancel it
    When order status is "IN_PROGRESS"
    Then the order can not be set to "CANCELLED"

  Scenario: order status is "Ready" and we want to cancel it
    When order status is "READY"
    Then the order can not be set to "CANCELLED"

  Scenario: order status is "obsolete" and we want to cancel it
    When order status is "OBSOLETE"
    Then the order can not be set to "CANCELLED"

  Scenario: order status is "cancelled" and we want to cancel it
    When order status is "CANCELLED"
    Then the order can not be set to "CANCELLED"

  Scenario: order status is "completed" and we want to cancel it
    When order status is "COMPLETED"
    Then the order can not be set to "CANCELLED"

  Scenario: order status is "not started" and we want to cancel it
    When order status is "NOT_STARTED"
    Then the order can be set to "CANCELLED"

  Scenario: order status is "payed" and we want to cancel it
    When order status is "PAYED"
    Then the order can be set to "CANCELLED"


# we can't pass an order to the status that it's already in
  Scenario: order status is already "in progress" and we want to pass it to "in progress"
    When order status is "IN_PROGRESS"
    Then the order can not be set to "IN_PROGRESS"

  Scenario: order status is already "PAYED" and we want to pass it to "PAYED"
    When order status is "PAYED"
    Then the order can not be set to "PAYED"

  Scenario: order status is "payed" and we want to pass it to "completed"
    When order status is "PAYED"
    Then the order can not be set to "COMPLETED"

  Scenario: order status is "in progress" and we want to pass it to "completed"
    When order status is "IN_PROGRESS"
    Then the order can not be set to "COMPLETED"

  Scenario: order status is "completed" and we want to pass it to "obsolete"
    When order status is "COMPLETED"
    Then the order can not be set to "OBSOLETE"

# allowed set status changes

  Scenario: order status is "not started" and we want to cancel it
    When order status is "NOT_STARTED"
    Then the order can be set to "CANCELLED"

  Scenario: order status is "payed" and we want to cancel it
    When order status is "PAYED"
    Then the order can be set to "CANCELLED"

  Scenario: order status is "in progress" and we want to pass it to "ready"
    When order status is "IN_PROGRESS"
    Then the order can be set to "READY"

  Scenario: order status is "ready" and we want to pass it to "completed"
    When order status is "READY"
    Then the order can be set to "COMPLETED"

  Scenario: order status is "payed" and we want to pass it to "in progress"
    When order status is "PAYED"
    Then the order can be set to "IN_PROGRESS"

  Scenario: order status is "payed" and we want to pass it to "ready"
    When order status is "PAYED"
    Then the order can be set to "READY"

  Scenario: order not found
    When an order
    Then the order can not be found and canceled