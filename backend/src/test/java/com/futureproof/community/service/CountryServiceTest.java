package com.futureproof.community.service;

import com.futureproof.community.models.Animal;
import com.futureproof.community.models.AnimalList;
import com.futureproof.community.models.Country;
import com.futureproof.community.repository.CountryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CountryServiceTest {

    private final CountryRepository countryRepository = mock(CountryRepository.class);
    private final JunctionService junctionService = mock(JunctionService.class);

    private final CountryService countryService = new CountryService(countryRepository, junctionService);

    @Test
    void shouldReturnCountryFromDatabaseById() {
        Country country = new Country(1, "france");

        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        Country countryResult = countryService.getCountry(1L);

        assertThat(countryResult, equalTo(country));
        verify(countryRepository).findById(1L);
    }

    @Test
    void shouldThrowNoSuchElementExceptionIfInvalidId() {
        when(countryRepository.findById(1L)).thenReturn(empty());

        assertThrows(NoSuchElementException.class, () -> countryService.getCountry(1L));

        verify(countryRepository).findById(1L);
    }

    @Test
    void shouldSaveCountryToDatabaseIfNotPresent() {
        Country country = new Country(1, "france");

        when(countryRepository.findById(1L)).thenReturn(empty());
        when(countryRepository.save(country)).thenReturn(country);

        Country countryResult = countryService.saveCountry(country);

        assertThat(countryResult, equalTo(country));
        verify(countryRepository).save(country);
    }

    @Test
    void shouldThrowErrorIfTryingToSaveWithTakenId() {
        Country country = new Country(1, "france");

        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        assertThrows(IllegalArgumentException.class, () -> countryService.saveCountry(country));
    }

    @Test
    void shouldDeleteCountryFromDatabaseById() {
        countryService.deleteCountry(1L);

        verify(countryRepository).deleteById(1L);
    }

    @Test
    void shouldUpdateAnimalInTheDatabase() {
        Country country = new Country(1, "france");

        when(countryRepository.save(country)).thenReturn(country);

        Country countryResult = countryService.updateCountry(country);

        assertThat(countryResult, equalTo(country));
        verify(countryRepository).save(country);
    }

    @Test
    void shouldReturnAnimalListForACountryId() {
        Animal animal1 = new Animal(1, "test1", "test1", 0.00, 0.00);
        Animal animal2 = new Animal(2, "test2", "test2", 0.00, 0.00);
        AnimalList animalList = new AnimalList(List.of(animal1, animal2));

        when(junctionService.getAnimalsForCountry(1L)).thenReturn(animalList);

        AnimalList animalsForCountry = countryService.getAnimalsForCountry(1L);

        assertThat(animalsForCountry, equalTo(animalList));
        verify(junctionService).getAnimalsForCountry(1L);
    }

    @Test
    void shouldSaveAJunctionInTheDatabase() {
        countryService.newAnimalInCountry(1L, 1L);

        verify(junctionService).saveJunction(1L, 1L);
    }

    @Test
    void shouldDeleteAJunctionFromTheDatabase() {
        countryService.animalRemovedFromCountry(1L, 1L);

        verify(junctionService).deleteJunction(1L, 1L);
    }
}