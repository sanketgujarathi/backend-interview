package org.deblock.exercise.infrastructure.adapters.crazyair;

import static org.deblock.exercise.domain.FlightSupplierType.CRAZYAIR;
import static org.springframework.http.HttpMethod.GET;

import java.time.LocalDate;
import java.util.List;

import org.deblock.exercise.domain.Flight;
import org.deblock.exercise.domain.FlightSupplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@Component
public class CrazyAirFlightSupplierImpl implements FlightSupplier {

    private final String crazyAirApiUrl;
    private final RestTemplate restTemplate;

    public CrazyAirFlightSupplierImpl(@Value("${supplier.crazyAir.url}") String crazyAirApiUrl,
                                      RestTemplate restTemplate) {
        this.crazyAirApiUrl = crazyAirApiUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Flight> getFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate,
                                   int numberOfPassengers) {

        ResponseEntity<List<CrazyAirResponse>> response =
            restTemplate.exchange(crazyAirApiUrl, GET, null, new ParameterizedTypeReference<>() {
            });

        return CollectionUtils.isEmpty(response.getBody()) ? List.of() : response.getBody()
            .stream()
            .map(this::toFlight)
            .toList();
    }

    private Flight toFlight(CrazyAirResponse response) {
        return new Flight(response.getAirline(), CRAZYAIR, response.getPrice(),
            response.getDepartureAirportCode(), response.getDestinationAirportCode(),
            response.getDepartureDate(), response.getArrivalDate());
    }
}
