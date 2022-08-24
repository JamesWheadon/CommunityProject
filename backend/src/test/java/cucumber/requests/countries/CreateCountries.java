package cucumber.requests.countries;

import com.futureproof.community.models.Animal;
import com.futureproof.community.models.Country;
import com.futureproof.community.repository.CountryRepository;
import com.futureproof.community.models.ErrorResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import static com.futureproof.community.util.Constants.ERROR_SAVING_COUNTRY;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

public class CreateCountries {

    public static final String COUNTRY_NAME = "france";
    @LocalServerPort
    private int port;

    @Autowired
    private CountryRepository countryRepository;

    public Response response;

    @When("a country is saved")
    public void aCountryIsSaved() {
        Country country = new Country(1, COUNTRY_NAME);

        response = given().contentType(JSON).port(port).body(country)
                .then().response().contentType(JSON)
                .when().post("/countries");
    }

    @Then("the country is saved and returned")
    public void theCountryIsSavedAndReturned() {
        assertThat(response.statusCode(), equalTo(CREATED.value()));
        assertThat(response.getBody().as(Animal.class).getName(), equalTo(COUNTRY_NAME));
        assertThat(countryRepository.findById(1L).isPresent(), equalTo(true));
        assertThat(countryRepository.findById(1L).get().getName(), equalTo(COUNTRY_NAME));
    }

    @Then("a save country error is returned")
    public void aSaveCountryErrorIsReturned() {
        assertThat(response.statusCode(), equalTo(BAD_REQUEST.value()));
        assertThat(response.getBody().as(ErrorResponse.class).getErrorMessage(), equalTo(ERROR_SAVING_COUNTRY));
    }
}
