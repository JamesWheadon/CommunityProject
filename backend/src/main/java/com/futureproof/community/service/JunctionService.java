package com.futureproof.community.service;

import com.futureproof.community.models.AnimalList;
import com.futureproof.community.models.CountryList;
import com.futureproof.community.models.Junction;
import com.futureproof.community.repository.AnimalRepository;
import com.futureproof.community.repository.CountryRepository;
import com.futureproof.community.repository.JunctionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JunctionService {

    private final AnimalRepository animalRepository;
    private final CountryRepository countryRepository;
    private final JunctionRepository junctionRepository;

    public JunctionService(AnimalRepository animalRepository,
                           CountryRepository countryRepository,
                           JunctionRepository junctionRepository) {
        this.animalRepository = animalRepository;
        this.countryRepository = countryRepository;
        this.junctionRepository = junctionRepository;
    }

    public CountryList getCountriesForAnimal(long animalId) {
        List<Junction> animalCountries = junctionRepository.findAllByAnimalId(animalId);
        List<Long> countryIds = animalCountries.stream().map(Junction::getCountryId).collect(Collectors.toList());
        return new CountryList(countryRepository.findAllById(countryIds));
    }

    public AnimalList getAnimalsForCountry(long countryId) {
        List<Junction> countryAnimals = junctionRepository.findAllByCountryId(countryId);
        List<Long> animalIds = countryAnimals.stream().map(Junction::getAnimalId).collect(Collectors.toList());
        return new AnimalList(animalRepository.findAllById(animalIds));
    }

    public void saveJunction(long animalId, long countryId) {
        List<Junction> existingJunctions = junctionRepository.findAllByAnimalIdAndCountryId(animalId, countryId);
        if (existingJunctions.isEmpty()) {
            junctionRepository.save(new Junction(animalId, countryId));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void deleteJunction(long animalId, long countryId) {
        junctionRepository.deleteByAnimalIdAndCountryId(animalId, countryId);
    }
}
