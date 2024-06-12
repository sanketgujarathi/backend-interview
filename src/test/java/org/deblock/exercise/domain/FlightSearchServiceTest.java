package org.deblock.exercise.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.deblock.exercise.domain.FlightSupplierType.CRAZYAIR;
import static org.deblock.exercise.domain.FlightSupplierType.TOUGHJET;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlightSearchServiceTest {
    private FlightSupplier crazyAirClient;
    private FlightSupplier toughJetClient;
    private FlightSearchService flightSearchService;

    @BeforeEach
    void setUp() {
        crazyAirClient = Mockito.mock(FlightSupplier.class);
        toughJetClient = Mockito.mock(FlightSupplier.class);
        flightSearchService = new FlightSearchService(List.of(crazyAirClient, toughJetClient));
    }

    @Test
    void shouldReturnFlightsSortedByFare() {

        Flight flight1 = new Flight("Airline1", CRAZYAIR, BigDecimal.valueOf(200.0), "LHR", "AMS", LocalDate.now()
            .atStartOfDay(), LocalDate.now()
            .plusDays(1)
            .atStartOfDay());
        Flight flight2 = new Flight("Airline2", TOUGHJET, BigDecimal.valueOf(150.0), "LHR", "AMS", LocalDate.now()
            .atStartOfDay(), LocalDate.now()
            .plusDays(1)
            .atStartOfDay());

        when(crazyAirClient.getFlights("LHR", "EDI", LocalDate.now(), LocalDate.now(), 2)).thenReturn(List.of(flight1));
        when(toughJetClient.getFlights("LHR", "EDI", LocalDate.now(), LocalDate.now(), 2)).thenReturn(List.of(flight2));

        List<Flight> result = flightSearchService.searchFlights("LHR", "EDI", LocalDate.now(), LocalDate.now(), 2);

        verify(crazyAirClient).getFlights("LHR", "EDI", LocalDate.now(), LocalDate.now(), 2);
        verify(toughJetClient).getFlights("LHR", "EDI", LocalDate.now(), LocalDate.now(), 2);
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(flight2);
        assertThat(result.get(1)).isEqualTo(flight1);
    }
}