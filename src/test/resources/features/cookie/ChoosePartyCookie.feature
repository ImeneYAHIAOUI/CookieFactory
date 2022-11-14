  # Created by Pauline at 11/11/2022
  Feature: choose a party cookie and an amount
  # Enter feature description here
    Background:
      Given a cod to store data
      And A client with phone number "0606060606"
      And a specific time of the day

    Scenario: Choose an available party cookies
      Given an inventory with the ingredients and amounts
        | chocolate chips       | 6 |
        | chocolate dough       | 4 |
        | vanillaFlavour        | 2 |
        | strawberryFlavour     | 3 |
        | chocolateFlavour      | 2 |
        | white chocolate chips | 4 |

      And a store with id 1

      And the store has cookies
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
        | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
        | strawberry | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |
      And the store has party cookies and all available size
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
        | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
        | strawberry | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |

      When Client chooses party cookies of type "chocolala" and size "L"
      And amount 1


      Then this order can be purchased
      And the clients cart contains 1 party cookie(s) of type "chocolala" and size "L"


    Scenario: Choose party cookies with wrong size
      Given an inventory with the ingredients and amounts
        | chocolate chips       | 6 |
        | chocolate dough       | 4 |
        | vanillaFlavour        | 2 |
        | strawberryFlavour     | 3 |
        | chocolateFlavour      | 2 |
        | white chocolate chips | 4 |

      And a store with id 1

      And the store has cookies
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
        | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
        | strawberry | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |
      And the store has party cookies and all available size
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
        | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
        | strawberry | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |

      When Client chooses party cookies of type "chocolala" and size "M"
      And amount 1

      Then this order cannot be purchased because the cookie doesn't exist
      And the clients cart is empty


    Scenario: Choose party cookies that doesn't exist
      Given an inventory with the ingredients and amounts
        | chocolate chips       | 6 |
        | chocolate dough       | 4 |
        | vanillaFlavour        | 2 |
        | strawberryFlavour     | 3 |
        | chocolateFlavour      | 2 |
        | white chocolate chips | 4 |

      And a store with id 1

      And the store has cookies
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
        | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      And the store has party cookies and all available size
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
        | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |

      When Client chooses party cookies of type "chocoWrong" and size "L"
      And amount 1

      Then this order cannot be purchased because the cookie doesn't exist
      And the clients cart is empty


    Scenario: Choose party cookies with wrong amount
      Given an inventory with the ingredients and amounts
        | chocolate chips       | 6 |
        | chocolate dough       | 4 |
        | vanillaFlavour        | 2 |
        | strawberryFlavour     | 3 |
        | chocolateFlavour      | 2 |
        | white chocolate chips | 4 |

      And a store with id 1

      And the store has cookies
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
        | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      And the store has party cookies and all available size
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
        | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |

      When Client chooses party cookies of type "chocoWrong" and size "L"
      And amount -1

      Then this order cannot be purchased because the cookie doesn't exist
      And the clients cart is empty

    Scenario: Choose many party cookies
      Given an inventory with the ingredients and amounts
        | chocolate chips       | 6 |
        | chocolate dough       | 8 |
        | vanillaFlavour        | 10 |
        | strawberryFlavour     | 10 |
        | chocolateFlavour      | 10 |
        | white chocolate chips | 10 |

      And a store with id 1

      And the store has cookies
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
        | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      And the store has party cookies and all available size
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
        | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |

      When Client chooses party cookies of type "chocolala" and size "XL"
      And amount 2

      Then this order can be purchased
      And the clients cart contains 2 party cookie(s) of type "chocolala" and size "XL"


    Scenario: More Ingredients are used for a party cookie size XL
      Given an inventory with the ingredients and amounts
        | chocolate chips       | 3 |
        | chocolate dough       | 3 |
        | chocolateFlavour      | 3 |
        | white chocolate chips | 3 |


      And a store with id 1

      And the store has cookies
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      And the store has party cookies and all available size
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |

      When Client chooses party cookies of type "chocolala" and size "XL"
      #XL -> Ingredients x 3
      And amount 1
      And client finalize his order

      Then the inventory has no ingredients left


    Scenario: More Ingredients are used for a party cookie size XXL
      Given an inventory with the ingredients and amounts
        | chocolate chips       | 4 |
        | chocolate dough       | 4 |
        | chocolateFlavour      | 4 |
        | white chocolate chips | 4 |

      And a store with id 1

      And the store has cookies
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      And the store has party cookies and all available size
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |

      When Client chooses party cookies of type "chocolala" and size "XXL"
      #XXL -> Ingredients x 4
      And amount 1
      And client finalize his order

      Then the inventory has no ingredients left


    Scenario: More Ingredients are used for a party cookie size L
      Given an inventory with the ingredients and amounts
        | chocolate chips       | 2 |
        | chocolate dough       | 2 |
        | chocolateFlavour      | 2 |
        | white chocolate chips | 2 |


      And a store with id 1

      And the store has cookies
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      And the store has party cookies and all available size
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |

      When Client chooses party cookies of type "chocolala" and size "L"
      #L -> Ingredients x 2
      And amount 1
      And client finalize his order

      Then the inventory has no ingredients left


    Scenario: Higher Price for a party cookie with size XL than a normal cookie
      Given an inventory with the ingredients and amounts
        | chocolate chips       | 12 |
        | chocolate dough       | 12 |
        | chocolateFlavour      | 12 |
        | white chocolate chips | 12 |


      And a store with id 1
      And the store has party cookies with all size and all themes
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |

      When Client chooses party cookies of type "chocolala" and size "XL" and theme "ANIMAL"
      And amount 1
      And client finalize his order

      Then the price of the order is 25.0
      #prix cookie normal = 5.0, taille XL -> prix x5 = 25.0

    Scenario: Higher Price for a party cookie with size XXL than a normal cookie
      Given an inventory with the ingredients and amounts
        | chocolate chips       | 12 |
        | chocolate dough       | 12 |
        | chocolateFlavour      | 12 |
        | white chocolate chips | 12 |


      And a store with id 1
      And the store has party cookies with all size and all themes
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |

      When Client chooses party cookies of type "chocolala" and size "XXL" and theme "ANIMAL"
      And amount 1
      And client finalize his order

      Then the price of the order is 30.0
      #prix cookie normal = 5.0, taille XL -> prix x6 = 30.0

    Scenario: Order a party cookie with a theme
      Given an inventory with the ingredients and amounts
        | chocolate chips       | 2 |
        | chocolate dough       | 2 |
        | chocolateFlavour      | 2 |
        | white chocolate chips | 2 |


      And a store with id 1
      And the store has party cookies with all size and all themes
        | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |

      When Client chooses party cookies of type "chocolala" and size "L" and theme "ANIMAL"
      And amount 1
      And client finalize his order

      Then the inventory has no ingredients left