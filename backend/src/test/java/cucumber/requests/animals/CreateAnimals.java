package cucumber.requests.animals;

import com.futureproof.community.models.Animal;
import com.futureproof.community.repository.AnimalRepository;
import com.futureproof.community.models.ErrorResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import static com.futureproof.community.util.Constants.ERROR_SAVING_ANIMAL;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

public class CreateAnimals {

    public static final String ANIMAL_NAME = "test3";

    @LocalServerPort
    private int port;

    @Autowired
    private AnimalRepository animalRepository;

    public Response response;

    @When("an animal is saved")
    public void anAnimalIsSaved() {
        Animal animal = new Animal(1, ANIMAL_NAME, ANIMAL_NAME, 0.00, 0.00);

        response = given().contentType(JSON).port(port).body(animal)
                .then().response().contentType(JSON)
                .when().post("/animals");
    }

    @Then("the animal is saved and returned")
    public void theAnimalIsSavedAndReturned() {
        assertThat(response.statusCode(), equalTo(CREATED.value()));
        assertThat(response.getBody().as(Animal.class).getName(), equalTo(ANIMAL_NAME));
        assertThat(animalRepository.findById(1L).isPresent(), equalTo(true));
        assertThat(animalRepository.findById(1L).get().getName(), equalTo(ANIMAL_NAME));
    }

    @Then("a save error is returned")
    public void aSaveErrorIsReturned() {
        assertThat(response.statusCode(), equalTo(BAD_REQUEST.value()));
        assertThat(response.getBody().as(ErrorResponse.class).getErrorMessage(), equalTo(ERROR_SAVING_ANIMAL));
    }
}
