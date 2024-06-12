package org.deblock.exercise.application.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightResponse(String airline, String supplier, BigDecimal fare, String departureAirportCode,
                             String destinationAirportCode, LocalDateTime departureDate, LocalDateTime arrivalDate) {
}
