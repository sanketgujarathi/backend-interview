package org.deblock.exercise.domain;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

@Service
public class FlightSearchService {

    private final List<FlightSupplier> flightSuppliers;

    public FlightSearchService(List<FlightSupplier> flightSuppliers) {
        this.flightSuppliers = flightSuppliers;
    }

    public List<Flight> searchFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate,
                                      int numberOfPassengers) {

        List<CompletableFuture<List<Flight>>> futures = flightSuppliers.stream()
            .map(supplier -> CompletableFuture.supplyAsync(
                () -> supplier.getFlights(origin, destination, departureDate, returnDate, numberOfPassengers)))
            .toList();

        return futures.stream()
            .map(CompletableFuture::join)
            .flatMap(List::stream)
            .sorted(Comparator.comparing(Flight::getFare))
            .toList();
    }
}
