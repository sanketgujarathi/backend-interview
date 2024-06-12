package org.deblock.exercise.infrastructure.adapters.toughjet;

import static org.deblock.exercise.domain.FlightSupplierType.TOUGHJET;
import static org.springframework.http.HttpMethod.GET;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class ToughJetFlightSupplierImpl implements FlightSupplier {

    private final String toughJetApiUrl;
    private final RestTemplate restTemplate;

    public ToughJetFlightSupplierImpl(@Value("${supplier.toughJet.url}") String toughJetApiUrl,
                                      RestTemplate restTemplate) {
        this.toughJetApiUrl = toughJetApiUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Flight> getFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate,
                                   int numberOfPassengers) {

        ResponseEntity<List<ToughJetResponse>> response = restTemplate.exchange(toughJetApiUrl, GET, null,
            new ParameterizedTypeReference<>() {
            });

        return CollectionUtils.isEmpty(response.getBody()) ? List.of() : response.getBody()
            .stream()
            .map(this::toFlight)
            .toList();
    }

    private Flight toFlight(ToughJetResponse response) {
        BigDecimal fare = calculateFare(response).setScale(2, RoundingMode.DOWN);
        return new Flight(response.getCarrier(), TOUGHJET, fare,
            response.getDepartureAirportName(), response.getArrivalAirportName(),
            response.getOutboundDateTime(), response.getInboundDateTime());
    }

    private BigDecimal calculateFare(ToughJetResponse response) {
        return response.getBasePrice()
            .add(response.getTax())
            .subtract(calculateDiscount(response));
    }

    private BigDecimal calculateDiscount(ToughJetResponse response) {
        return response.getBasePrice()
            .multiply(response.getDiscount()
                .divide(BigDecimal.valueOf(100), RoundingMode.DOWN));
    }
}
