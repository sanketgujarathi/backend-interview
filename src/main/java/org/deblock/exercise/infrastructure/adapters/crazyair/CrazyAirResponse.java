package org.deblock.exercise.infrastructure.adapters.crazyair;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CrazyAirResponse {
    private String airline;
    private BigDecimal price;
    private String departureAirportCode;
    private String destinationAirportCode;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
}
