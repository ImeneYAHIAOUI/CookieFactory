# Created by Floriane PARIS at 10/24/2022

Feature: Register a client

  Background:
    Given an empty cod without data

  Scenario: Register
    When Client register with id "id", mdp "mdp", phone number 456
    Then Cod clients is not empty
    And Cod client contains client with id "id", mdp "mdp", phone number 456
    And Cod client contains 1 element