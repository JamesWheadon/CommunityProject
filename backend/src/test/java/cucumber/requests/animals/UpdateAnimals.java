package cucumber.requests.animals;

import com.futureproof.community.models.Animal;
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
import static org.springframework.http.HttpStatus.CREATED;

public class UpdateAnimals {

    public static final String ANIMAL_NAME = "test3";

    @LocalServerPort
    private int port;

    @Autowired
    private AnimalRepository animalRepository;

    public Response response;

    @When("an animal is updated")
    public void anAnimalIsUpdated() {
        assertThat(animalRepository.findById(1L).isPresent(), equalTo(true));
        assertThat(animalRepository.findById(1L).get().getName(), equalTo("test1"));

        Animal animal = new Animal(1, ANIMAL_NAME, ANIMAL_NAME, 0.00, 0.00);

        response = given().contentType(JSON).port(port).body(animal)
                .then().response().contentType(JSON)
                .when().put("/animals");
    }

    @Then("the updated animal is returned")
    public void theUpdatedAnimalIsReturned() {
        assertThat(response.statusCode(), equalTo(CREATED.value()));
        assertThat(response.getBody().as(Animal.class).getName(), equalTo(ANIMAL_NAME));
        assertThat(animalRepository.findById(1L).isPresent(), equalTo(true));
        assertThat(animalRepository.findById(1L).get().getName(), equalTo(ANIMAL_NAME));
    }
}
