package org.deblock.exercise.infrastructure.adapters.toughjet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ToughJetResponse {
    private String carrier;
    private BigDecimal basePrice;
    private BigDecimal tax;
    private BigDecimal discount;
    private String departureAirportName;
    private String arrivalAirportName;
    private LocalDateTime outboundDateTime;
    private LocalDateTime inboundDateTime;
}
