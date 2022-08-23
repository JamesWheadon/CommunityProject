package cucumber.populate;

import com.futureproof.community.models.Country;
import com.futureproof.community.repository.CountryRepository;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Countries {

    @Autowired
    private CountryRepository countryRepository;

    @And("countries are present in the database")
    public void countriesArePresentInTheDatabase() {
        Country france = new Country("france");
        Country spain = new Country("spain");
        countryRepository.saveAll(List.of(france, spain));
    }
}
