package com.futureproof.community.controllers;

import com.futureproof.community.models.Animal;
import com.futureproof.community.models.AnimalList;
import com.futureproof.community.models.CountryList;
import com.futureproof.community.service.AnimalService;
import com.futureproof.community.models.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

import static com.futureproof.community.util.Constants.ERROR_SAVING_ANIMAL;
import static com.futureproof.community.util.Constants.INVALID_ANIMAL_ID;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "All animals retrieved from database")})
    @GetMapping(value = "/animals", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<AnimalList> getAnimals() {
        return ok(animalService.getAnimals());
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Animal retrieved from database by ID", content = {@Content(schema = @Schema(implementation = Animal.class))}),
            @ApiResponse(responseCode = "400", description = "ID does not match an animal in the database", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping(value = "/animals/{id}", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Animal> getAnimalById(@PathVariable long id) {
        return ok(animalService.getAnimal(id));
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created animal in the database", content = {@Content(schema = @Schema(implementation = Animal.class))}),
            @ApiResponse(responseCode = "400", description = "Animal already exists in the database", content = {@Content(schema = @Schema(implementation = Animal.class))})})
    @PostMapping(value = "/animals", consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Animal> saveAnimal(@RequestBody Animal animal) {
        return ResponseEntity.status(CREATED).body(animalService.saveAnimal(animal));
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Animal deleted from the database by ID")})
    @DeleteMapping(value = "/animals/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable long id) {
        animalService.deleteAnimal(id);
        return noContent().build();
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Animal updated in the database")})
    @PutMapping(value = "/animals", consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Animal> updateAnimal(@RequestBody Animal animal) {
        return ResponseEntity.status(CREATED).body(animalService.updateAnimal(animal));
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "All countries animal introduced to retrieved from database")})
    @GetMapping(value = "/animals/{id}/countries", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<CountryList> getCountriesByAnimalId(@PathVariable long id) {
        return ok(animalService.getCountriesForAnimal(id));
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchElementException() {
        return badRequest().body(new ErrorResponse(INVALID_ANIMAL_ID));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException() {
        return badRequest().body(new ErrorResponse(ERROR_SAVING_ANIMAL));
    }
}
