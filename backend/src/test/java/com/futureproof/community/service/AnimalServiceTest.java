package com.futureproof.community.service;

import com.futureproof.community.models.Animal;
import com.futureproof.community.models.AnimalList;
import com.futureproof.community.models.Country;
import com.futureproof.community.models.CountryList;
import com.futureproof.community.repository.AnimalRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AnimalServiceTest {

    private final AnimalRepository animalRepository = mock(AnimalRepository.class);
    private final JunctionService junctionService = mock(JunctionService.class);

    private final AnimalService animalService = new AnimalService(animalRepository, junctionService);

    @Test
    void shouldReturnAllAnimalsFromDatabase() {
        Animal animal = new Animal(1, "test", "test", 0.00, 0.00);

        when(animalRepository.findAll()).thenReturn(List.of(animal));

        AnimalList animals = animalService.getAnimals();

        assertThat(animals.getAnimals().size(), equalTo(1));
        assertThat(animals.getAnimals().get(0), equalTo(animal));
        verify(animalRepository).findAll();
    }

    @Test
    void shouldReturnAnimalFromDatabaseById() {
        Animal animal = new Animal(1, "test", "test", 0.00, 0.00);

        when(animalRepository.findById(any())).thenReturn(Optional.of(animal));

        Animal animalResult = animalService.getAnimal(1L);

        assertThat(animalResult, equalTo(animal));
        verify(animalRepository).findById(any());
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfInvalidId() {
        when(animalRepository.findById(any())).thenReturn(empty());

        assertThrows(NoSuchElementException.class, () -> animalService.getAnimal(1L));

        verify(animalRepository).findById(any());
    }

    @Test
    void shouldSaveAnimalToDatabaseIfNotPresent() {
        Animal animal = new Animal(1, "test", "test", 0.00, 0.00);

        when(animalRepository.findById(any())).thenReturn(empty());
        when(animalRepository.save(any())).thenReturn(animal);

        Animal animalResult = animalService.saveAnimal(animal);

        assertThat(animalResult, equalTo(animal));
        verify(animalRepository).save(any());
    }

    @Test
    void shouldThrowErrorIfTryingToSaveWithTakenId() {
        Animal animal = new Animal(1, "test", "test", 0.00, 0.00);

        when(animalRepository.findById(any())).thenReturn(Optional.of(animal));

        assertThrows(IllegalArgumentException.class, () -> animalService.saveAnimal(animal));
    }

    @Test
    void shouldDeleteAnimalFromDatabaseById() {
        animalService.deleteAnimal(1);

        verify(animalRepository).deleteById(any());
    }

    @Test
    void shouldUpdateAnimalInTheDatabase() {
        Animal animal = new Animal(1, "test", "test", 0.00, 0.00);

        when(animalRepository.save(any())).thenReturn(animal);

        Animal animalResult = animalService.updateAnimal(animal);

        assertThat(animalResult, equalTo(animal));
        verify(animalRepository).save(any());
    }

    @Test
    void shouldReturnAListOfCountriesForAnAnimalId() {
        Country france = new Country(1, "france");
        Country spain = new Country(2, "spain");
        CountryList countryList = new CountryList(List.of(france, spain));

        when(junctionService.getCountriesForAnimal(1L)).thenReturn(countryList);

        CountryList countriesForAnimal = animalService.getCountriesForAnimal(1L);

        assertThat(countriesForAnimal, equalTo(countryList));
        verify(junctionService).getCountriesForAnimal(1L);
    }
}