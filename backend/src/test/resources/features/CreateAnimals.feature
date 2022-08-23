Feature: Users can save animals to the database

  Scenario: A user can save an animal to the database
    When an animal is saved
    Then the animal is saved and returned

  Scenario: A user cant save an animal with an existing animals ID
    Given animals are present in the database
    When an animal is saved
    Then a save error is returned
