package com.performance.api.core.hall;

import java.util.List;

public record Hall(
	Integer hallId,
	String hallName,
	List<Seat> seatList,

	String serviceAddress

) {
}
