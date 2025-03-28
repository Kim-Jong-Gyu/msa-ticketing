package com.ticketing.api.composite;

public record CreatePerformance(
	Integer performanceId,
	ServiceAddresses serviceAddresses
) {
}
