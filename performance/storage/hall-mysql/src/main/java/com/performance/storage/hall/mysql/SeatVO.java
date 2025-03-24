package com.performance.storage.hall.mysql;

import com.ticketing.performance.common.SeatType;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SeatVO {

	Integer seatNumber;

	Character section;

	@Enumerated(EnumType.STRING)
	SeatType type;

	Boolean isAvailable;

}
