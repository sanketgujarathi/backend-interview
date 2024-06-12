package org.deblock.exercise.application.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.deblock.exercise.domain.FlightSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.github.tomakehurst.wiremock.WireMockServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"supplier.crazyAir.url=http://localhost:8081/crazyair/flights", "supplier.toughJet.url=http://localhost:8081/toughjet/flights"})
class FlightControllerITest {

    protected MockMvc mockMvc;
    @Autowired
    private FlightSearchService flightSearchService;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FlightController(flightSearchService))
            .setControllerAdvice(globalExceptionHandler)
            .build();
    }

    @Test
    void searchFlightsShouldReturnSuccessWhenValidRequest() throws Exception {
        WireMockServer wm = new WireMockServer(8081);
        configureFor("localhost", 8081);
        wm.stubFor(get(urlPathEqualTo("/crazyair/flights"))
            .willReturn(aResponse()
                .withBody(
                    "[{\"airline\":\"Airline1\",\"price\":200.0,\"departureAirportCode\":\"LHR\",\"destinationAirportCode\":\"EDI\",\"departureDate\":\"2023-12-01T00:00:00\",\"arrivalDate\":\"2023-12-01T00:00:00\"}]")
                .withHeader("Content-Type", "application/json")));

        wm.stubFor(get(urlPathEqualTo("/toughjet/flights"))
            .willReturn(aResponse()
                .withBody(
                    "[{\"carrier\":\"Airline2\",\"basePrice\":150.0,\"tax\":20.0,\"discount\":10.0,\"departureAirportName\":\"LHR\",\"arrivalAirportName\":\"EDI\",\"outboundDateTime\":\"2023-12-02T00:00:00Z\",\"inboundDateTime\":\"2023-12-02T00:00:00Z\"}]")
                .withHeader("Content-Type", "application/json")));

        wm.start();

        mockMvc.perform(MockMvcRequestBuilders.get("/flights")
                .queryParam("origin", "LHR")
                .queryParam("destination", "EDI")
                .queryParam("departureDate", "2024-12-01")
                .queryParam("returnDate", "2024-12-02")
                .queryParam("numberOfPassengers", "4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.flights", hasSize(2)));
    }

    @Test
    void searchFlightShouldReturnBadRequestWhenInvalidRequestParams() throws Exception {

        //invalid origin
        mockMvc.perform(MockMvcRequestBuilders.get("/flights")
                .queryParam("origin", "   ")
                .queryParam("destination", "EDI")
                .queryParam("departureDate", "2024-12-01")
                .queryParam("returnDate", "2024-12-02")
                .queryParam("numberOfPassengers", "4"))
            .andExpect(status().isBadRequest());

        //invalid destination
        mockMvc.perform(MockMvcRequestBuilders.get("/flights")
                .queryParam("origin", "LHR")
                .queryParam("destination", "EDIS")
                .queryParam("departureDate", "2024-12-01")
                .queryParam("returnDate", "2024-12-02")
                .queryParam("numberOfPassengers", "4"))
            .andExpect(status().isBadRequest());

        //invalid departure date
        mockMvc.perform(MockMvcRequestBuilders.get("/flights")
                .queryParam("origin", "LHR")
                .queryParam("destination", "EDI")
                .queryParam("departureDate", "01-12-2024")
                .queryParam("returnDate", "2024-12-02")
                .queryParam("numberOfPassengers", "4"))
            .andExpect(status().isBadRequest());

        //invalid return date
        mockMvc.perform(MockMvcRequestBuilders.get("/flights")
                .queryParam("origin", "LHR")
                .queryParam("destination", "EDI")
                .queryParam("departureDate", "2024-12-01")
                .queryParam("returnDate", "02-12-2024")
                .queryParam("numberOfPassengers", "4"))
            .andExpect(status().isBadRequest());

        //invalid number of passengers
        mockMvc.perform(MockMvcRequestBuilders.get("/flights")
                .queryParam("origin", "LHR")
                .queryParam("destination", "EDI")
                .queryParam("departureDate", "2024-12-01")
                .queryParam("returnDate", "2024-12-02")
                .queryParam("numberOfPassengers", "5"))
            .andExpect(status().isBadRequest());


    }
}