package com.futureproof.community.controllers;

import com.futureproof.community.models.Animal;
import com.futureproof.community.models.AnimalList;
import com.futureproof.community.models.Country;
import com.futureproof.community.models.ResponseMessage;
import com.futureproof.community.service.CountryService;
import com.futureproof.community.models.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

import static com.futureproof.community.util.Constants.ANIMAL_EXISTS_IN_COUNTRY;
import static com.futureproof.community.util.Constants.ERROR_SAVING_COUNTRY;
import static com.futureproof.community.util.Constants.INVALID_COUNTRY_ID;
import static com.futureproof.community.util.Constants.MAPPING_CREATED;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Country retrieved from database by ID", content = {@Content(schema = @Schema(implementation = Country.class))}),
            @ApiResponse(responseCode = "400", description = "ID does not match a country in the database", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping(value = "/countries/{id}", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getCountry(@PathVariable long id) {
        try {
            return ok(countryService.getCountry(id));
        } catch (NoSuchElementException e) {
            return badRequest().body(new ErrorResponse(INVALID_COUNTRY_ID));
        }
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created country in the database", content = {@Content(schema = @Schema(implementation = Country.class))}),
            @ApiResponse(responseCode = "400", description = "Country already exists in the database", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping(value = "/countries", consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> saveCountry(@RequestBody Country country) {
        try {
            return ResponseEntity.status(CREATED).body(countryService.saveCountry(country));
        } catch (IllegalArgumentException e) {
            return badRequest().body(new ErrorResponse(ERROR_SAVING_COUNTRY));
        }
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Country deleted from the database by ID")})
    @DeleteMapping(value = "/countries/{id}", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteCountry(@PathVariable long id) {
        countryService.deleteCountry(id);
        return noContent().build();
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Country updated in the database")})
    @PutMapping(value = "/countries", consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Country> updateCountry(@RequestBody Country country) {
        return ResponseEntity.status(CREATED).body(countryService.updateCountry(country));
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "All animals introduced to country retrieved from database")})
    @GetMapping(value = "/countries/{id}/animals", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<AnimalList> getCountriesByAnimalId(@PathVariable long id) {
        return ok(countryService.getAnimalsForCountry(id));
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "New mapping added in database linking animal and country", content = {@Content(schema = @Schema(implementation = ResponseMessage.class))}),
            @ApiResponse(responseCode = "400", description = "Mapping between animal and country already exists in database", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping(value = "/countries/{countryId}/animals/{animalId}", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> addNewAnimalToCountry(@PathVariable long countryId, @PathVariable long animalId) {
        try {
            countryService.newAnimalInCountry(animalId, countryId);
            return ResponseEntity.status(CREATED).body(new ResponseMessage(MAPPING_CREATED));
        } catch (IllegalArgumentException e) {
            return badRequest().body(new ErrorResponse(ANIMAL_EXISTS_IN_COUNTRY));
        }
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Mapping between animal and country deleted from database")})
    @DeleteMapping(value = "/countries/{countryId}/animals/{animalId}", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> removeAnimalFromCountry(@PathVariable long countryId, @PathVariable long animalId) {
        countryService.animalRemovedFromCountry(animalId, countryId);
        return noContent().build();
    }
}
