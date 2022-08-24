package com.futureproof.community.controllers;

import com.futureproof.community.models.Animal;
import com.futureproof.community.models.AnimalList;
import com.futureproof.community.models.Country;
import com.futureproof.community.service.CountryService;
import com.futureproof.community.models.ErrorResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.NoSuchElementException;

import static com.futureproof.community.util.Constants.ERROR_SAVING_COUNTRY;
import static com.futureproof.community.util.Constants.INVALID_COUNTRY_ID;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CountryControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    CountryService countryService;

    @Test
    void shouldReturnCountryFromDatabaseWith200() {
        Country country = new Country(1, "france");

        when(countryService.getCountry(1L)).thenReturn(country);

        Response response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().get("/countries/1");

        assertThat(response.statusCode(), equalTo(OK.value()));
        assertThat(response.getBody().as(Country.class), equalTo(country));
    }

    @Test
    void shouldReturnErrorWithInvalidWith400() {
        when(countryService.getCountry(1L)).thenThrow(new NoSuchElementException());

        Response response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().get("/countries/1");

        assertThat(response.statusCode(), equalTo(BAD_REQUEST.value()));
        assertThat(response.getBody().as(ErrorResponse.class).getErrorMessage(), equalTo(INVALID_COUNTRY_ID));
    }

    @Test
    void shouldReturnCreatedCountryWith201() {
        Country country = new Country("france");

        when(countryService.saveCountry(country)).thenReturn(country);

        Response response = given().contentType(JSON).port(port).body(country)
                .then().response().contentType(JSON)
                .when().post("/countries");

        assertThat(response.statusCode(), equalTo(CREATED.value()));
        assertThat(response.getBody().as(Country.class).getName(), equalTo(country.getName()));
    }

    @Test
    void shouldThrowErrorWhenTryingToSaveWithTakenId() {
        Country country = new Country("france");

        when(countryService.saveCountry(country)).thenThrow(new IllegalArgumentException());

        Response response = given().contentType(JSON).port(port).body(country)
                .then().response().contentType(JSON)
                .when().post("/countries");

        assertThat(response.statusCode(), equalTo(BAD_REQUEST.value()));
        assertThat(response.getBody().as(ErrorResponse.class).getErrorMessage(), equalTo(ERROR_SAVING_COUNTRY));
    }

    @Test
    void shouldDeleteCountryWith204() {
        Response response = given().contentType(JSON).port(port)
                .when().delete("/countries/1");

        assertThat(response.statusCode(), equalTo(NO_CONTENT.value()));
    }

    @Test
    void shouldUpdateCountryWith201() {
        Country country = new Country("france");

        when(countryService.updateCountry(country)).thenReturn(country);

        Response response = given().contentType(JSON).port(port).body(country)
                .then().response().contentType(JSON)
                .when().put("/countries");

        assertThat(response.statusCode(), equalTo(CREATED.value()));
        assertThat(response.getBody().as(Country.class).getName(), equalTo(country.getName()));
    }

    @Test
    void shouldGetAllAnimalsForCountryWith200() {
        Animal animal = new Animal(1, "test", "test", 0.00, 0.00);
        AnimalList animalList = new AnimalList(List.of(animal));

        when(countryService.getAnimalsForCountry(1L)).thenReturn(animalList);

        Response response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().get("/countries/1/animals");

        assertThat(response.statusCode(), equalTo(OK.value()));
        assertThat(response.getBody().as(AnimalList.class), equalTo(animalList));
    }

    @Test
    void shouldSaveNewJunctionAndReturn201() {
        Response response = given().contentType(JSON).port(port)
                .when().post("/countries/1/animals/1");

        assertThat(response.statusCode(), equalTo(CREATED.value()));
        verify(countryService).newAnimalInCountry(1L, 1L);
    }

    @Test
    void shouldNotSaveJunctionAndReturn400() {
        doThrow(new IllegalArgumentException()).when(countryService).newAnimalInCountry(1L, 1L);

        Response response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().post("/countries/1/animals/1");

        assertThat(response.statusCode(), equalTo(BAD_REQUEST.value()));
        verify(countryService).newAnimalInCountry(1L, 1L);
    }

    @Test
    void shouldDeleteJunctionFromDatabaseAndReturn204() {
        Response response = given().contentType(JSON).port(port)
                .when().delete("/countries/1/animals/1");

        assertThat(response.statusCode(), equalTo(NO_CONTENT.value()));
        verify(countryService).animalRemovedFromCountry(1L, 1L);
    }
}