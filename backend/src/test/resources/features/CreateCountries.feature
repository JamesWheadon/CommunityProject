Feature: Users can save countries to the database

  Scenario: A user can save a country to the database
    When a country is saved
    Then the country is saved and returned

  Scenario: A user cant save a country with an existing countries ID
    Given countries are present in the database
    When a country is saved
    Then a save country error is returned
