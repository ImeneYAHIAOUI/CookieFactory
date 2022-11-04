  # Created by imene at 10/20/2022
Feature: choose a cookie and an amount
  # Enter feature description here
  Background:
    Given a cod to store data
    And A client with phone number 0606060606




  Scenario: choose an available cookies
    Given an inventory with the ingredients and amounts
      | chocolate chips       | 6 |
      | chocolate dough       | 4 |
      | vanillaFlavour        | 2 |
      | strawberryFlavour     | 3 |
      | chocolateFlavour      | 8 |
      | white chocolate chips | 4 |

    And a store with id 1

    And the store has cookies
      | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      | strawbary | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |

    When Client chooses cookies of type "chocolala"
    And amount 1

    Then this order can be purchased
    And the clients card contains 1 cookie(s) of type "chocolala"



  Scenario: choose an available number of cookies
    Given an inventory with the ingredients and amounts
      | chocolate chips       | 6 |
      | chocolate dough       | 4 |
      | vanillaFlavour        | 2 |
      | strawberryFlavour     | 3 |
      | chocolateFlavour      | 8 |
      | white chocolate chips | 4 |

    And a store with id 1

    And the store has cookies
      | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      | strawbary | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |

    When Client chooses cookies of type "chocolala"
    And amount 4

    Then this order can be purchased
    And the clients card contains 4 cookie(s) of type "chocolala"

  Scenario: choose same cookie twice
    Given an inventory with the ingredients and amounts
      | chocolate chips       | 6 |
      | chocolate dough       | 5 |
      | vanillaFlavour        | 2 |
      | strawberryFlavour     | 3 |
      | chocolateFlavour      | 4 |
      | white chocolate chips | 5 |

    And a store with id 1

    And the store has cookies
      | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      | strawbary | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |

    When Client chooses cookies of type "chocolala"
    And amount 1

    Then this order can be purchased
    And the clients card contains 1 cookie(s) of type "chocolala"

    When Client chooses cookies of type "chocolala"
    And amount 3

    Then this order can be purchased
    And the clients card contains 4 cookie(s) of type "chocolala"



  Scenario: choose an unavailable amount of cookie
    Given an inventory with the ingredients and amounts
      | chocolate chips       | 6 |
      | chocolate dough       | 4 |
      | vanillaFlavour        | 2 |
      | strawberryFlavour     | 3 |
      | chocolateFlavour      | 8 |
      | white chocolate chips | 4 |

    And a store with id 1

    And the store has cookies
      | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      | strawbary | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |

    When Client chooses cookies of type "chocolala"
    And amount 5


    Then this order cannot be purchased

  Scenario: choose an unavailable cookie
    Given an inventory with the ingredients and amounts
      | chocolate chips       | 6 |
      | chocolate dough       | 4 |
      | vanillaFlavour        | 2 |
      | strawberryFlavour     | 3 |
      | chocolateFlavour      | 8 |
      | white chocolate chips | 4 |

    And a store with id 1

    And the store has cookies
      | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      | vanilla  | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      | strawbary | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |

    When Client chooses cookies of type "triple chocolate"
    And amount 4
    Then this order cannot be purchased

  Scenario: Registered client canceled one order in the last 10 minutes
    Given an inventory with the ingredients and amounts
      | chocolate chips       | 6 |
      | chocolate dough       | 4 |
      | vanillaFlavour        | 2 |
      | strawberryFlavour     | 3 |
      | chocolateFlavour      | 8 |
      | white chocolate chips | 4 |

    And a store with id 1

    And the store has cookies
      | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      | strawbary | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |
    And A registered client has canceled one order in the last 10 minutes
    Then the client is not banned

  Scenario: Registered client canceled two orders in eight minutes in the last 10 minutes
    Given an inventory with the ingredients and amounts
      | chocolate chips       | 6 |
      | chocolate dough       | 4 |
      | vanillaFlavour        | 2 |
      | strawberryFlavour     | 3 |
      | chocolateFlavour      | 8 |
      | white chocolate chips | 4 |

    And a store with id 1

    And the store has cookies
      | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      | strawbary | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |
    And A registered client has canceled two orders in the last 10 minutes
    Then the client is banned


  Scenario: Registered client canceled two orders in eight minutes more than 10 minutes ago
    Given an inventory with the ingredients and amounts
      | chocolate chips       | 6 |
      | chocolate dough       | 4 |
      | vanillaFlavour        | 2 |
      | strawberryFlavour     | 3 |
      | chocolateFlavour      | 8 |
      | white chocolate chips | 4 |

    And a store with id 1

    And the store has cookies
      | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      | strawbary | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |
    And A registered client has canceled two orders in more than 10 minutes
    Then the client is not banned

  Scenario: Registered client canceled two orders in more than eight minutes in the last 10 minutes
    Given an inventory with the ingredients and amounts
      | chocolate chips       | 6 |
      | chocolate dough       | 4 |
      | vanillaFlavour        | 2 |
      | strawberryFlavour     | 3 |
      | chocolateFlavour      | 8 |
      | white chocolate chips | 4 |

    And a store with id 1

    And the store has cookies
      | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      | strawbary | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |

    And A registered client has canceled two orders in within more than 8 minutes
    Then the client is not banned


  Scenario: Registered client hasn't canceled any orders in the last 10 minutes
    Given an inventory with the ingredients and amounts
      | chocolate chips       | 6 |
      | chocolate dough       | 4 |
      | vanillaFlavour        | 2 |
      | strawberryFlavour     | 3 |
      | chocolateFlavour      | 8 |
      | white chocolate chips | 4 |

    And a store with id 1

    And the store has cookies
      | chocolala | chocolate dough | chocolateFlavour | chocolate chips | white chocolate chips |
      | vanilla   | chocolate dough | vanillaFlavour   | white chocolate chips | chocolate chips |
      | strawbary | chocolate dough | strawberryFlavour | white chocolate chips | chocolate chips |

    And A registered client hasn't canceled any orders in the last 10 minutes
    Then the client is not banned