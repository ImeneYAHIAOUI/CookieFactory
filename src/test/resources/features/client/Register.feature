# Created by Floriane PARIS at 10/24/2022

Feature: Register a client

  Background:
    Given an empty cod without data

  Scenario: Register 1 client
    When Client register with id "id", mdp "mdp", phone number "0123456789"
    Then this client can register
    And Cod clients is not empty
    And Cod client contains client with id "id", mdp "mdp", phone number "0123456789"
    And Cod client contains 1 element

  Scenario: Register twice with different id
    When Client register with id "id", mdp "mdp", phone number "0123456789"
    And this client can register
    And Client register with id "id2", mdp "mdp2", phone number "0123456789"
    Then this client can register
    And Cod clients is not empty
    And Cod client contains client with id "id", mdp "mdp", phone number "0123456789"
    And Cod client contains client with id "id2", mdp "mdp2", phone number "0123456789"
    And Cod client contains 2 element

  Scenario: Register twice with same id
    When Client register with id "id", mdp "mdp", phone number "0123456789"
    And this client can register
    And Client register with id "id", mdp "mdp2", phone number "0123456789"
    Then this client can't register
    And Cod clients is not empty
    And Cod client contains client with id "id", mdp "mdp", phone number "0123456789"
    And Cod client contains 1 element
