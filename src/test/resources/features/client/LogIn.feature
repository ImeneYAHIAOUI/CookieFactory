# Created by Sourour GAZZEH at 29/10/2022

Feature: LogIn

  Background:
    Given repository client with registered client with mail "sgazzeh" , password "password" and phone number "+33 6-67-99-58-95"

  Scenario: logIn with valid input
    When Client log in  with mail "sgazzeh" and password "password"
    Then Client Manager connected clients is not empty
    And this client can logIn
  Scenario: logIn with invalid password
    When Client log in with invalid password :  mail "sgazzeh" and password "mdp"
    Then  Client Manager connected clients is empty
  Scenario: logIn with invalid id
    When Client log in with invalid id :  mail "id" and password "mdp"
    Then  Client Manager connected clients is empty


