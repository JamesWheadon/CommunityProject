package com.futureproof.community.controllers;

import com.futureproof.community.models.Animal;
import com.futureproof.community.models.AnimalList;
import com.futureproof.community.models.Country;
import com.futureproof.community.models.CountryList;
import com.futureproof.community.service.AnimalService;
import com.futureproof.community.models.ErrorResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.NoSuchElementException;

import static com.futureproof.community.util.Constants.ERROR_SAVING_ANIMAL;
import static com.futureproof.community.util.Constants.INVALID_ANIMAL_ID;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimalControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    AnimalService animalService;

    @Test
    void shouldReturnAllAnimalsFromDatabaseWith200() {
        Animal animal = new Animal(1, "test", "test", 0.00, 0.00);
        AnimalList animalList = new AnimalList(List.of(animal));
        when(animalService.getAnimals()).thenReturn(animalList);

        Response response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().get("/animals");

        assertThat(response.statusCode(), equalTo(OK.value()));
        assertThat(response.getBody().as(AnimalList.class).getAnimals().size(), equalTo(1));
        assertThat(response.getBody().as(AnimalList.class).getAnimals().get(0), equalTo(animal));
    }

    @Test
    void shouldReturnAnimalFromDatabaseByValidIdWith200() {
        Animal animal = new Animal(1, "test", "test", 0.00, 0.00);

        when(animalService.getAnimal(1)).thenReturn(animal);

        Response response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().get("/animals/1");

        assertThat(response.statusCode(), equalTo(OK.value()));
        assertThat(response.getBody().as(Animal.class), equalTo(animal));
    }

    @Test
    void shouldReturnErrorWithInvalidIdWith400() {
        when(animalService.getAnimal(1)).thenThrow(new NoSuchElementException());

        Response response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().get("/animals/1");

        assertThat(response.statusCode(), equalTo(BAD_REQUEST.value()));
        assertThat(response.getBody().as(ErrorResponse.class).getErrorMessage(), equalTo(INVALID_ANIMAL_ID));
    }

    @Test
    void shouldReturnCreatedAnimalWith201() {
        Animal animal = new Animal("test", "test", 0.00, 0.00);

        when(animalService.saveAnimal(any())).thenReturn(animal);

        Response response = given().contentType(JSON).port(port).body(animal)
                .then().response().contentType(JSON)
                .when().post("/animals");

        assertThat(response.statusCode(), equalTo(CREATED.value()));
        assertThat(response.getBody().as(Animal.class).getName(), equalTo(animal.getName()));
    }

    @Test
    void shouldThrowErrorWhenTryingToSaveWithTakenId() {
        Animal animal = new Animal("test", "test", 0.00, 0.00);

        when(animalService.saveAnimal(any())).thenThrow(new IllegalArgumentException());

        Response response = given().contentType(JSON).port(port).body(animal)
                .then().response().contentType(JSON)
                .when().post("/animals");

        assertThat(response.statusCode(), equalTo(BAD_REQUEST.value()));
        assertThat(response.getBody().as(ErrorResponse.class).getErrorMessage(), equalTo(ERROR_SAVING_ANIMAL));
    }

    @Test
    void shouldDeleteAnAnimalWith204() {
        Response response = given().contentType(JSON).port(port)
                .when().delete("/animals/1");

        assertThat(response.statusCode(), equalTo(NO_CONTENT.value()));
    }

    @Test
    void shouldUpdateAnimalWith201() {
        Animal animal = new Animal("test", "test", 0.00, 0.00);

        when(animalService.updateAnimal(any())).thenReturn(animal);

        Response response = given().contentType(JSON).port(port).body(animal)
                .then().response().contentType(JSON)
                .when().put("/animals");

        assertThat(response.statusCode(), equalTo(CREATED.value()));
        assertThat(response.getBody().as(Animal.class).getName(), equalTo(animal.getName()));
    }

    @Test
    void shouldGetAllCountriesForAnAnimalWith200() {
        Country france = new Country(1, "france");
        Country spain = new Country(2, "spain");
        CountryList countryList = new CountryList(List.of(france, spain));

        when(animalService.getCountriesForAnimal(1L)).thenReturn(countryList);

        Response response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().get("/animals/1/countries");

        assertThat(response.statusCode(), equalTo(OK.value()));
        assertThat(response.getBody().as(CountryList.class), equalTo(countryList));
    }
}