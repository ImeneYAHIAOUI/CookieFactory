# Created by Sourour GAZZEH at 29/10/2022

Feature: LogIn

  Background:
    Given cod with registered client with id "sgazzeh" , password "password" and phone number "0123456789"

  Scenario: logIn with valid input
    When Client log in  with id "sgazzeh" and password "password"
    Then Cod connected clients is not empty
    And this client can logIn
  Scenario: logIn with invalid password
    When Client log in with invalid password :  id "sgazzeh" and password "mdp"
    Then  Cod connected clients is empty
  Scenario: logIn with invalid id
    When Client log in with invalid id :  id "id" and password "mdp"
    Then  Cod connected clients is empty


