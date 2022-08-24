package cucumber.requests.animals;

import com.futureproof.community.models.Animal;
import com.futureproof.community.models.AnimalList;
import com.futureproof.community.models.CountryList;
import com.futureproof.community.models.ErrorResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Objects;

import static com.futureproof.community.util.Constants.INVALID_ANIMAL_ID;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class GetAnimals {

    @LocalServerPort
    private int port;

    public Response response;

    @When("all animals are requested")
    public void allAnimalsAreRequested() {
        response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().get("/animals");
    }

    @When("an animal is requested with a {string} id")
    public void anAnimalIsRequest(String validity) {
        String path;
        if (Objects.equals(validity, "valid")) {
            path = "/animals/1";
        } else {
            path = "/animals/10";
        }
        response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().get(path);
    }

    @When("the countries for an animal are requested")
    public void theCountriesForAnAnimalAreRequested() {
        response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().get("/animals/1/countries");
    }

    @Then("all animals are returned")
    public void allAnimalsAreReturned() {
        assertThat(response.statusCode(), equalTo(OK.value()));
        assertThat(response.getBody().as(AnimalList.class).getAnimals().size(), equalTo(2));
        assertThat(getAnimalFromAnimalList(response, 0).getName(), equalTo("test1"));
        assertThat(getAnimalFromAnimalList(response, 1).getName(), equalTo("test2"));
    }

    @Then("the animal is returned")
    public void theAnimalIsReturned() {
        assertThat(response.statusCode(), equalTo(OK.value()));
        assertThat(response.getBody().as(Animal.class).getName(), equalTo("test1"));
    }

    @Then("a list of countries is returned")
    public void aListOfCountriesIsReturned() {
        assertThat(response.statusCode(), equalTo(OK.value()));
        assertThat(response.getBody().as(CountryList.class).getCountries().size(), equalTo(2));
        assertThat(response.getBody().as(CountryList.class).getCountries().get(0).getName(), equalTo("france"));
    }

    @Then("a get error is returned")
    public void anErrorIsReturned() {
        assertThat(response.statusCode(), equalTo(BAD_REQUEST.value()));
        assertThat(response.getBody().as(ErrorResponse.class).getErrorMessage(), equalTo(INVALID_ANIMAL_ID));
    }

    private Animal getAnimalFromAnimalList(Response response, int index) {
        return response.getBody().as(AnimalList.class).getAnimals().get(index);
    }
}
