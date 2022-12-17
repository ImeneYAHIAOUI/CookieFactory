# Created by Floriane PARIS at 10/24/2022

Feature: Register a client

  Background:
    Given an empty client repository

  Scenario: Register 1 client
    When Client register with mail "mail", mdp "mdp", phone number "0123456789"
    Then this client can register
    And Cod clients is not empty
    And client repository contains client with mail "mail", mdp "mdp", phone number "0123456789"
    And repository client contains 1 element

  Scenario: Register twice with different id
    When Client register with mail "mail", mdp "mdp", phone number "0123456789"
    And this client can register
    And Client register with mail "mail2", mdp "mdp2", phone number "0123456789"
    Then this client can register
    And Cod clients is not empty
    And client repository contains client with mail "mail", mdp "mdp", phone number "0123456789"
    And client repository contains client with mail "mail2", mdp "mdp2", phone number "0123456789"
    And repository client contains 2 element

  Scenario: Register twice with same id
    When Client register with mail "mail", mdp "mdp", phone number "0123456789"
    And this client can register
    And Client register with mail "mail", mdp "mdp2", phone number "0123456789"
    Then this client can't register
    And Cod clients is not empty
    And client repository contains client with mail "mail", mdp "mdp", phone number "0123456789"
    And repository client contains 1 element
