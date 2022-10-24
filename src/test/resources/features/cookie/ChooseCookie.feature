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
    And the inventory has the ingredients and amounts
      | chocolate chips       | 5 |
      | chocolate dough       | 3 |
      | vanillaFlavour        | 2 |
      | strawberryFlavour     | 3 |
      | chocolateFlavour      | 7 |
      | white chocolate chips | 3 |



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

    And the inventory has the ingredients and amounts
      | chocolate chips       | 2 |
      | chocolate dough       | 0 |
      | vanillaFlavour        | 2 |
      | strawberryFlavour     | 3 |
      | chocolateFlavour      | 4 |
      | white chocolate chips | 0 |

    And the store is out of
      | chocolala |
      | vanilla |
      | strawbary |

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


