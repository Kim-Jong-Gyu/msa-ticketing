package com.ticketing.api.composite;

public record CreateHall(
	Integer hallId,
	ServiceAddresses serviceAddresses
) {
}
