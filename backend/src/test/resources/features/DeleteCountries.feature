Feature: Delete country by ID

  Scenario: A user can delete a country from the database
    Given countries are present in the database
    When a country is deleted
    Then the country is removed from the database
