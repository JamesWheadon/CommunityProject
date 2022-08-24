package cucumber.populate;

import com.futureproof.community.models.Junction;
import com.futureproof.community.repository.JunctionRepository;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Arrays.asList;

public class Junctions {

    @Autowired
    private JunctionRepository junctionRepository;

    @And("junctions are present in the database")
    public void junctionsArePresentInTheDatabase() {
        Junction junction1 = new Junction(1, 1);
        Junction junction2 = new Junction(1, 2);
        junctionRepository.saveAll(asList(junction1, junction2));
    }
}
