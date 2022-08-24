package cucumber.requests.countries;

import com.futureproof.community.models.Animal;
import com.futureproof.community.models.Country;
import com.futureproof.community.repository.AnimalRepository;
import com.futureproof.community.repository.CountryRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.CREATED;

public class UpdateCountries {

    public static final String COUNTRY_NAME = "germany";

    @LocalServerPort
    private int port;

    @Autowired
    private CountryRepository countryRepository;

    public Response response;

    @When("a country is updated")
    public void aCountryIsUpdated() {
        assertThat(countryRepository.findById(1L).isPresent(), equalTo(true));
        assertThat(countryRepository.findById(1L).get().getName(), equalTo("france"));

        Country country = new Country(1, COUNTRY_NAME);

        response = given().contentType(JSON).port(port).body(country)
                .then().response().contentType(JSON)
                .when().put("/countries");
    }

    @Then("the updated country is returned")
    public void theUpdatedCountryIsReturned() {
        assertThat(response.statusCode(), equalTo(CREATED.value()));
        assertThat(response.getBody().as(Animal.class).getName(), equalTo(COUNTRY_NAME));
        assertThat(countryRepository.findById(1L).isPresent(), equalTo(true));
        assertThat(countryRepository.findById(1L).get().getName(), equalTo(COUNTRY_NAME));
    }
}
