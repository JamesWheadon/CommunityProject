Feature: Update an animal in the database

  Scenario: A user can update an animal in the database
    Given animals are present in the database
    When an animal is updated
    Then the updated animal is returned
