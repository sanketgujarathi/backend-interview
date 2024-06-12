package org.deblock.exercise.application.request;

import java.time.LocalDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public record FlightSearchRequest(@NotBlank @Size(min = 3, max = 3) String origin,
                                  @NotBlank @Size(min = 3, max = 3) String destination,
                                  @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
                                  @Min(1) @Max(4) int numberOfPassengers) {
}
