package cucumber.populate;

import com.futureproof.community.models.Animal;
import com.futureproof.community.repository.AnimalRepository;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Arrays.asList;

public class Animals {

    @Autowired
    private AnimalRepository animalRepository;

    @Given("animals are present in the database")
    public void animalsArePresentInTheDatabase() {
        Animal animal1 = new Animal("test1", "test1", 0.00, 0.00);
        Animal animal2 = new Animal("test2", "test2", 100.00, 80.00);
        animalRepository.saveAll(asList(animal1, animal2));
    }
}
