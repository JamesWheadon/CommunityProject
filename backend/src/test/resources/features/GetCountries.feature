Feature: Retrieve countries by ID and animals in countries

  Scenario: A user can retrieve a country with a valid id
    Given countries are present in the database
    When a country is requested with a "valid" ID
    Then the country is returned

  Scenario: A user requests an animal using an invalid id
    Given countries are present in the database
    When a country is requested with a "invalid" ID
    Then a get country error is returned

  Scenario: A user requests the animals for an country
    Given animals are present in the database
    And junctions are present in the database
    And countries are present in the database
    When the animals for an country are requested
    Then a list of animals is returned