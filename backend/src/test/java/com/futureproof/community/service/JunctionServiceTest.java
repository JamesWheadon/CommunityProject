package com.futureproof.community.service;

import com.futureproof.community.models.Animal;
import com.futureproof.community.models.AnimalList;
import com.futureproof.community.models.Country;
import com.futureproof.community.models.CountryList;
import com.futureproof.community.models.Junction;
import com.futureproof.community.repository.AnimalRepository;
import com.futureproof.community.repository.CountryRepository;
import com.futureproof.community.repository.JunctionRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JunctionServiceTest {

    private final AnimalRepository animalRepository = mock(AnimalRepository.class);
    private final CountryRepository countryRepository = mock(CountryRepository.class);
    private final JunctionRepository junctionRepository = mock(JunctionRepository.class);

    private final JunctionService junctionService = new JunctionService(animalRepository, countryRepository, junctionRepository);

    @Test
    void shouldReturnAListOfCountriesForAnAnimalId() {
        Country france = new Country(1, "france");
        Country spain = new Country(2, "spain");
        List<Junction> animalCountries = List.of(new Junction(1, 1, 1), new Junction(1, 1, 2));
        List<Long> countryIds = List.of(1L, 2L);

        when(junctionRepository.findAllByAnimalId(1L)).thenReturn(animalCountries);
        when(countryRepository.findAllById(countryIds)).thenReturn(List.of(france, spain));

        CountryList countryIdsForAnimal = junctionService.getCountriesForAnimal(1L);

        assertThat(countryIdsForAnimal.getCountries().size(), equalTo(2));
        assertThat(countryIdsForAnimal.getCountries().get(0), equalTo(france));
        verify(junctionRepository).findAllByAnimalId(1L);
        verify(countryRepository).findAllById(countryIds);
    }

    @Test
    void shouldReturnAListOfAnimalsForACountryId() {
        Animal animal1 = new Animal(1, "test1", "test1", 0.00, 0.00);
        Animal animal2 = new Animal(2, "test2", "test2", 0.00, 0.00);
        List<Junction> countryAnimals = List.of(new Junction(1, 1, 1), new Junction(1, 2, 1));
        List<Long> animalIds = List.of(1L, 2L);

        when(junctionRepository.findAllByCountryId(1L)).thenReturn(countryAnimals);
        when(animalRepository.findAllById(animalIds)).thenReturn(List.of(animal1, animal2));

        AnimalList animalIdsForCountry = junctionService.getAnimalsForCountry(1L);

        assertThat(animalIdsForCountry.getAnimals().size(), equalTo(2));
        assertThat(animalIdsForCountry.getAnimals().get(0), equalTo(animal1));
        verify(junctionRepository).findAllByCountryId(1L);
        verify(animalRepository).findAllById(animalIds);
    }

    @Test
    void shouldSaveNewJunctionToDatabase() {
        Junction junction = new Junction(1, 1, 1);

        when(junctionRepository.findAllByAnimalIdAndCountryId(1L, 1L)).thenReturn(emptyList());
        when(junctionRepository.save(any())).thenReturn(junction);

        junctionService.saveJunction(1L, 1L);

        verify(junctionRepository).findAllByAnimalIdAndCountryId(1L, 1L);
        verify(junctionRepository).save(any());
    }

    @Test
    void shouldThrowErrorIfJunctionAlreadyExists() {
        Junction junction = new Junction(1, 1, 1);

        when(junctionRepository.findAllByAnimalIdAndCountryId(1L, 1L)).thenReturn(List.of(junction));

        assertThrows(IllegalArgumentException.class, () -> junctionService.saveJunction(1L, 1L));

        verify(junctionRepository).findAllByAnimalIdAndCountryId(1L, 1L);
    }

    @Test
    void shouldDeleteJunctionFromDatabase() {
        junctionService.deleteJunction(1L, 1L);

        verify(junctionRepository).deleteByAnimalIdAndCountryId(1L, 1L);
    }
}