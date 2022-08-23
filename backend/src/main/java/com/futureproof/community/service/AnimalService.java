package com.futureproof.community.service;

import com.futureproof.community.models.Animal;
import com.futureproof.community.models.AnimalList;
import com.futureproof.community.models.CountryList;
import com.futureproof.community.repository.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final JunctionService junctionService;

    public AnimalService(AnimalRepository animalRepository, JunctionService junctionService) {
        this.animalRepository = animalRepository;
        this.junctionService = junctionService;
    }

    public AnimalList getAnimals() {
        return new AnimalList(animalRepository.findAll());
    }

    public Animal getAnimal(long id) {
        Optional<Animal> animal = animalRepository.findById(id);
        if (animal.isPresent()) {
            return animal.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    public Animal saveAnimal(Animal animal) {
        Optional<Animal> idAnimal = animalRepository.findById(animal.getId());
        if (idAnimal.isEmpty()) {
            return animalRepository.save(animal);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void deleteAnimal(long id) {
        animalRepository.deleteById(id);
    }

    public Animal updateAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    public CountryList getCountriesForAnimal(long animalId) {
        return junctionService.getCountriesForAnimal(animalId);
    }
}
