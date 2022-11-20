# Created by imene at 11/20/2022
Feature: choose a pickup time for later date
  # Enter feature description here

  @choose-pickup-time
  Scenario: Choose pickup time
    Given the client chooses a valid pickup time for the date "20/12/2022"
    When the client chooses a valid pickup time
    Then the pickup time is set to the client's cart

  @choose-pickup-time
  Scenario: Choose pickup time not in the valid pickup time list
    Given the client chooses a pickup time that is not in the valid pickup time list for the date "20/12/2022"
    When the client chooses a pickup time that is not in the valid pickup time list
    Then the pickup time is not set to the client's cart

  @choose-pickup-time
  Scenario: Choose pickup time on an invalid date
    When the client chooses an invalid date
    Then the pickup time is not set to the client's cart
