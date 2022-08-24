package com.futureproof.community.service;

import com.futureproof.community.models.AnimalList;
import com.futureproof.community.models.Country;
import com.futureproof.community.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final JunctionService junctionService;

    public CountryService(CountryRepository countryRepository, JunctionService junctionService) {
        this.countryRepository = countryRepository;
        this.junctionService = junctionService;
    }

    public Country getCountry(long countryId) {
        Optional<Country> country = countryRepository.findById(countryId);
        if (country.isPresent()) {
            return country.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    public Country saveCountry(Country country) {
        Optional<Country> idCountry = countryRepository.findById(country.getId());
        if (idCountry.isEmpty()) {
            return countryRepository.save(country);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void deleteCountry(long countryId) {
        countryRepository.deleteById(countryId);
    }

    public Country updateCountry(Country country) {
        return countryRepository.save(country);
    }

    public AnimalList getAnimalsForCountry(long countryId) {
        return junctionService.getAnimalsForCountry(countryId);
    }

    public void newAnimalInCountry(long animalId, long countryId) {
        junctionService.saveJunction(animalId, countryId);
    }

    public void animalRemovedFromCountry(long animalId, long countryId) {
        junctionService.deleteJunction(animalId, countryId);
    }
}
