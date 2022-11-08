# Created by Imene at 11/8/2022

Feature: get nearby stores

Background: I want to get nearby stores
  Given stores with locations
    | 950 Rte des Colles, 06410 Biot, France |
    | 2004 Rte des Lucioles, 06902 Valbonne, France |
    | 120 Rte des Macarons, 06560 Valbonne, France |
    | 2400 Rte des Dolines, 06560 Valbonne, France |
    | 231 Avenue Maurice Donat, 06700 Saint-Laurent-du-Var  |
    | 107 Av. de Nice, 06600 Antibes, France |
  And client located at "210 Av. Roumanille, 06410 Biot"


  Scenario: get nearby stores

    When The client wants to retrieve nearby stores
    Then The client should receive the stores with these locations
      | 950 Rte des Colles, 06410 Biot, France |
      | 2004 Rte des Lucioles, 06902 Valbonne, France |
      | 2400 Rte des Dolines, 06560 Valbonne, France |
      | 120 Rte des Macarons, 06560 Valbonne, France |
    And not these locations
      | 107 Av. de Nice, 06600 Antibes, France |
      | 231 Avenue Maurice Donat, 06700 Saint-Laurent-du-Var  |

  Scenario: get stores with a specific distance

    When The client wants to retrieve stores 10 "km" away
    Then The client should receive the stores with these locations
      | 950 Rte des Colles, 06410 Biot, France |
      | 2004 Rte des Lucioles, 06902 Valbonne, France |
      | 2400 Rte des Dolines, 06560 Valbonne, France |
      | 120 Rte des Macarons, 06560 Valbonne, France |
      | 107 Av. de Nice, 06600 Antibes, France |
      | 231 Avenue Maurice Donat, 06700 Saint-Laurent-du-Var  |

  Scenario: get stores with a specific distance 2

    When The client wants to retrieve stores 760 "m" away
    Then The client should receive the stores with these locations
      | 950 Rte des Colles, 06410 Biot, France |
      | 2004 Rte des Lucioles, 06902 Valbonne, France |
    And not these locations
      | 120 Rte des Macarons, 06560 Valbonne, France |
      | 107 Av. de Nice, 06600 Antibes, France |
      | 2400 Rte des Dolines, 06560 Valbonne, France |
      | 231 Avenue Maurice Donat, 06700 Saint-Laurent-du-Var  |


