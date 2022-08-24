Feature: Users can edit animals in countries

  Scenario: A user adds a new animal to a country
    Given animals are present in the database
    And countries are present in the database
    And junctions are present in the database
    When a new "valid" junction is added
    Then the junction is added to the database

  Scenario: A user adds an existing animal to a country
    Given animals are present in the database
    And countries are present in the database
    And junctions are present in the database
    When a new "invalid" junction is added
    Then the junction is not added and an error is returned

  Scenario: A user can remove an animal from a country
    Given animals are present in the database
    And countries are present in the database
    And junctions are present in the database
    When a junction is removed
    Then the junction is removed from the database
