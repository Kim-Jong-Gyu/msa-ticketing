package com.performance.api.core.hall;

import com.performance.api.enums.SeatType;

public record Seat(
	Integer seatNumber,
	Character section,
	SeatType type,
	Boolean isAvailable
) {
}
