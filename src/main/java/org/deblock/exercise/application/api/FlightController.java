package org.deblock.exercise.application.api;

import java.util.List;

import javax.validation.Valid;

import org.deblock.exercise.application.request.FlightSearchRequest;
import org.deblock.exercise.application.response.FlightResponse;
import org.deblock.exercise.application.response.FlightSearchResponse;
import org.deblock.exercise.domain.Flight;
import org.deblock.exercise.domain.FlightSearchService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flights")
@Validated
public class FlightController {
    private final FlightSearchService flightSearchService;

    public FlightController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    @GetMapping
    public FlightSearchResponse searchFlights(@Valid FlightSearchRequest flightSearchRequest) {
        List<FlightResponse> flights = flightSearchService.searchFlights(flightSearchRequest.origin(),
                flightSearchRequest.destination(), flightSearchRequest.departureDate(), flightSearchRequest.returnDate(),
                flightSearchRequest.numberOfPassengers())
            .stream()
            .map(this::mapToFlightResponse)
            .toList();
        return new FlightSearchResponse(flights);
    }

    private FlightResponse mapToFlightResponse(Flight flight) {
        return new FlightResponse(flight.getAirline(), flight.getSupplier()
            .name(), flight.getFare(), flight.getDepartureAirportCode(), flight.getDestinationAirportCode(),
            flight.getDepartureDate(), flight.getArrivalDate());
    }

}
