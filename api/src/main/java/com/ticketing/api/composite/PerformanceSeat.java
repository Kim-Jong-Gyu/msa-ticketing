package com.ticketing.api.composite;

public record PerformanceSeat(
	Integer seatNumber,
	Character section,
	Boolean isAvailable,
	Integer price

) {
}
