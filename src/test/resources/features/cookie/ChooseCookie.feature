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
      | chocolala |
      | vanilla   |
      | m&ms      |

    When Client chooses cookies of type "chocolala"
    And amount 1
    And recipe
      | chocolate dough         |
      | chocolateFlavour        |
      | white chocolate chips   |
      | chocolate chips         |

    Then this order can be purchased

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
      | chocolala |
    #  | vanilla   |
      #  | m&ms      |

    When Client chooses cookies of type "chocolala"
    And amount 4
    And recipe
      | chocolate dough         |
      | chocolateFlavour        |
      | white chocolate chips   |
      | chocolate chips         |

    Then this order can be purchased

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
      | chocolala |
      | vanilla   |
      | m&ms      |

    When Client chooses cookies of type "chocolala"
    And amount 5
    And recipe
      | chocolate dough         |
      | chocolateFlavour        |
      | white chocolate chips   |
      | chocolate chips         |

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
      | chocolala |
      | vanilla   |
      | m&ms      |

    When Client chooses cookies of type "triple chocolate"
    And amount 4
    And recipe
      | chocolate dough         |
      | chocolateFlavour        |
      | white chocolate chips   |
      | chocolate chips         |

    Then this order cannot be purchased


