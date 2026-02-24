package com.skyscanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "hoen-scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) {
        // This is usually left empty in this task
    }

    @Override
    public void run(final HoenScannerConfiguration configuration, final Environment environment) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // 1. Load the Rental Car results from the JSON file
        List<SearchResult> carResults = Arrays.asList(
                mapper.readValue(getClass().getClassLoader().getResourceAsStream("rental_cars.json"), SearchResult[].class)
        );

        // 2. Load the Hotel results from the JSON file
        List<SearchResult> hotelResults = Arrays.asList(
                mapper.readValue(getClass().getClassLoader().getResourceAsStream("hotels.json"), SearchResult[].class)
        );

        // 3. Combine both lists into one big list
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.addAll(carResults);
        searchResults.addAll(hotelResults);

        // 4. Register the SearchResource so the API knows where to find our logic
        final SearchResource resource = new SearchResource(searchResults);
        environment.jersey().register(resource);
    }
}
