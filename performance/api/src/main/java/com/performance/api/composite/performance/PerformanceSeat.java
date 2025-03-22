package com.performance.api.composite.performance;

public record PerformanceSeat(
	Integer seatNumber,
	Character section,

	Boolean isAvailable,
	Integer price

) {
}
