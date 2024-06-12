package org.deblock.exercise.application.response;

import java.util.List;

public record FlightSearchResponse(List<FlightResponse> flights) {
}
