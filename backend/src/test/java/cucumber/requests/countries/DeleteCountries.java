package cucumber.requests.countries;

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
import static org.springframework.http.HttpStatus.NO_CONTENT;

public class DeleteCountries {

    @LocalServerPort
    private int port;

    @Autowired
    private CountryRepository countryRepository;

    public Response response;

    @When("a country is deleted")
    public void aCountryIsDeleted() {
        response = given().contentType(JSON).port(port)
                .when().delete("/countries/1");
    }

    @Then("the country is removed from the database")
    public void theCountryIsRemovedFromTheDatabase() {
        assertThat(response.statusCode(), equalTo(NO_CONTENT.value()));
        assertThat(countryRepository.findById(1L).isPresent(), equalTo(false));
    }
}
