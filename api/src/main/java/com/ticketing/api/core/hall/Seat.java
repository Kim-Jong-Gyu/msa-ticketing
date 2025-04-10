package com.ticketing.api.core.hall;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Seat {

	private Integer seatNumber;
	private Character section;
	private String type;

	public Seat(Integer seatNumber, Character section, String type) {
		this.seatNumber = seatNumber;
		this.section = section;
		this.type = type;
	}
}
