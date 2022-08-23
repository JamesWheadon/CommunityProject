Feature: Update a country in the database

  Scenario: A user can update a country in the database
    Given countries are present in the database
    When a country is updated
    Then the updated country is returned