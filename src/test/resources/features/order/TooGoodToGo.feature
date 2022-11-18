# Created by joel at 13/11/2022
Feature: TooGoodToGo

  Scenario: Bag creation from obsolete orders
    Given a cod with a store having an order in progress
    When the order becomes ready
    And the order is not picked up after 2 hours
    Then the order is obsolete
    And a toogoodtogo bag is created

  Scenario: Bag creation but there are no orders
    Given a cod with a store having no orders in progress
    When the toogoodtogo check is performed
    Then no bag is created

  Scenario Outline: Bag creation with some obsolete orders and some not
    Given a cod with a store having <nbObsolete> obsolete orders and <nbOrder> <status>
    When the toogoodtogo check is performed
    Then the <nbObsolete> obsolete orders are converted
    Examples:
      | nbObsolete | nbOrder | status        |
      | 23         | 34      | "READY"       |
      | 2          | 12      | "COMPLETED"   |
      | 10         | 0       | "IN_PROGRESS" |
      | 24         | 1       | "NOT_STARTED" |
      | 0          | 0       | "COMPLETED"   |