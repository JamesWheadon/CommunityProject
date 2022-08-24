package cucumber.requests.junctions;

import com.futureproof.community.repository.CountryRepository;
import com.futureproof.community.repository.JunctionRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Objects;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

public class JunctionSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private JunctionRepository junctionRepository;

    public Response response;

    @When("a new {string} junction is added")
    public void aNewJunctionIsAdded(String validity) {
        String path;
        if (Objects.equals(validity, "valid")) {
            path = "/countries/1/animals/2";
        } else {
            path = "/countries/1/animals/1";
        }
        response = given().contentType(JSON).port(port)
                .when().post(path);
    }

    @When("a junction is removed")
    public void aJunctionIsRemoved() {
        response = given().contentType(JSON).port(port)
                .when().delete("/countries/1/animals/1");
    }

    @Then("the junction is added to the database")
    public void theJunctionIsAddedToTheDatabase() {
        assertThat(response.statusCode(), equalTo(CREATED.value()));
        assertThat(junctionRepository.findAll().size(), equalTo(3));
        assertThat((int) junctionRepository.findAll().stream()
                .filter(junction -> junction.getAnimalId() == 2 && junction.getCountryId() == 1)
                .count(), equalTo(1));
    }

    @Then("the junction is not added and an error is returned")
    public void theJunctionIsNotAddedAndAnErrorIsReturned() {
        assertThat(response.statusCode(), equalTo(BAD_REQUEST.value()));
        assertThat(junctionRepository.findAll().size(), equalTo(2));
    }

    @Then("the junction is removed from the database")
    public void theJunctionIsRemovedFromTheDatabase() {
        assertThat(response.statusCode(), equalTo(NO_CONTENT.value()));
        assertThat(junctionRepository.findAll().size(), equalTo(1));
        assertThat((int) junctionRepository.findAll().stream()
                .filter(junction -> junction.getAnimalId() == 1 && junction.getCountryId() == 1)
                .count(), equalTo(0));
    }
}
