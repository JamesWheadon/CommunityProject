Feature: Delete animal by ID

  Scenario: A user can delete an animal from the database
    Given animals are present in the database
    When an animal is deleted
    Then the animal is removed from the database
