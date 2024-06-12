package org.deblock.exercise.domain;

import java.time.LocalDate;
import java.util.List;

public interface FlightSupplier {

    List<Flight> getFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate,
                            int numberOfPassengers);
}
