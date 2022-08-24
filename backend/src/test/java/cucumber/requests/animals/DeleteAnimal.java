package cucumber.requests.animals;

import com.futureproof.community.repository.AnimalRepository;
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

public class DeleteAnimal {

    @LocalServerPort
    private int port;

    @Autowired
    private AnimalRepository animalRepository;

    public Response response;

    @When("an animal is deleted")
    public void anAnimalIsDeleted() {
        response = given().contentType(JSON).port(port)
                .when().delete("/animals/1");
    }

    @Then("the animal is removed from the database")
    public void theAnimalIsRemovedFromTheDatabase() {
        assertThat(response.statusCode(), equalTo(NO_CONTENT.value()));
        assertThat(animalRepository.findById(1L).isPresent(), equalTo(false));
    }
}
