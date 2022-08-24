package cucumber.requests.countries;

import com.futureproof.community.models.AnimalList;
import com.futureproof.community.models.Country;
import com.futureproof.community.models.ErrorResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Objects;

import static com.futureproof.community.util.Constants.INVALID_COUNTRY_ID;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class GetCountries {

    @LocalServerPort
    private int port;

    public Response response;

    @When("a country is requested with a {string} ID")
    public void aCountryIsRequested(String validity) {
        String path;
        if (Objects.equals(validity, "valid")) {
            path = "/countries/1";
        } else {
            path = "/countries/10";
        }
        response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().get(path);
    }

    @When("the animals for an country are requested")
    public void theAnimalsForAnCountryAreRequested() {
        response = given().contentType(JSON).port(port)
                .then().response().contentType(JSON)
                .when().get("/countries/1/animals");
    }

    @Then("the country is returned")
    public void theCountryIsReturned() {
        assertThat(response.statusCode(), equalTo(OK.value()));
        assertThat(response.getBody().as(Country.class).getName(), equalTo("france"));
    }

    @Then("a get country error is returned")
    public void aGetCountryErrorIsReturned() {
        assertThat(response.statusCode(), equalTo(BAD_REQUEST.value()));
        assertThat(response.getBody().as(ErrorResponse.class).getErrorMessage(), equalTo(INVALID_COUNTRY_ID));
    }

    @Then("a list of animals is returned")
    public void aListOfAnimalsIsReturned() {
        assertThat(response.statusCode(), equalTo(OK.value()));
        assertThat(response.getBody().as(AnimalList.class).getAnimals().size(), equalTo(1));
        assertThat(response.getBody().as(AnimalList.class).getAnimals().get(0).getName(), equalTo("test1"));
    }
}
