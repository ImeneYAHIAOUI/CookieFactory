# Created by Sourour GAZZEH at 29/10/2022

Feature: Modify quantity in cart

  Background:
    Given a  store with recipes
      | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      | strawbary | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |

  Scenario: registered client can modify quantity in cart
    Given unregistered client  with phone number "0667995895"
    When the client choose 1 cookie with name "chocolala"
    Then in his cart there is 1 cookie with name "chocolala"
    When he add another 1 cookie with name "chocolala"
    Then in his cart he has 2 cookies with name "chocolala"

  Scenario: Unregistered client increase quantity in cart
    Given registered client  with phone number "0667995895"
    When the client choose 1 cookie with name "chocolala"
    Then in his cart there is 1 cookie with name "chocolala"
    When he add another 1 cookie with name "chocolala"
    Then in his cart he has 2 cookies with name "chocolala"

  Scenario: registered client can remove cookie from cart
    Given registered client  with phone number "0667995895"
    When the client choose 2 cookie with name "chocolala"
    Then in his cart there is 2 cookie with name "chocolala"
    When he remove 1 cookie with name "chocolala"
    Then in his cart he has 1 cookie with name "chocolala"

  Scenario: Unregistered client can remove cookie from cart
    Given unregistered client  with phone number "0667995895"
    When the client choose 2 cookie with name "chocolala"
    Then in his cart there is 2 cookie with name "chocolala"
    When he remove 1 cookie with name "chocolala"
    Then in his cart he has 1 cookie with name "chocolala"

  Scenario: registered client can remove cookie from cart
    Given registered client  with phone number "0667995895"
    When the client choose 2 cookie with name "chocolala"
    Then in his cart there is 2 cookie with name "chocolala"
    When he remove 2 cookie with name "chocolala"
    Then his cart is empty

  Scenario: Unregistered client can remove cookie from cart
    Given unregistered client  with phone number "0667995895"
    When the client choose 2 cookie with name "chocolala"
    Then in his cart there is 2 cookie with name "chocolala"
    When he remove 2 cookie with name "chocolala"
    Then his cart is empty
  Scenario: Unregistered client can remove all cookies chosen
    Given registered client  with phone number "0667995895"
    When the client choose 1 cookie with name "chocolala"
    Then in his cart there is 1 cookie with name "chocolala"
    When he remove all items in cart
    Then his cart is empty

  Scenario: registered client can remove all cookies chosen
    Given unregistered client  with phone number "0667995895"
    When the client choose 1 cookie with name "chocolala"
    Then in his cart there is 1 cookie with name "chocolala"
    When he remove all items in cart
    Then his cart is empty