Feature: Retrieve all animals and animal by ID and countries by animal

  Scenario: A user can retrieve all animals
    Given animals are present in the database
    When all animals are requested
    Then all animals are returned

  Scenario: A user can retrieve an animal with a valid id
    Given animals are present in the database
    When an animal is requested with a "valid" id
    Then the animal is returned

  Scenario: A user requests an animal using an invalid id
    Given animals are present in the database
    When an animal is requested with a "invalid" id
    Then a get error is returned

  Scenario: A user requests the countries for an animal
    Given animals are present in the database
    And junctions are present in the database
    And countries are present in the database
    When the countries for an animal are requested
    Then a list of countries is returned
