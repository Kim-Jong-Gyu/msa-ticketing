package com.ticketing.api.composite;

public record CreateReservation(
	Integer reservationId,
	ServiceAddresses serviceAddresses
) {
}
