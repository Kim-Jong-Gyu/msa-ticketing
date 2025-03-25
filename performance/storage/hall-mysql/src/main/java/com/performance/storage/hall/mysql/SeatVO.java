package com.performance.storage.hall.mysql;

import com.ticketing.performance.common.SeatType;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatVO {

	Integer seatNumber;

	Character section;

	@Enumerated(EnumType.STRING)
	SeatType type;

	Boolean isAvailable;

	public SeatVO(Integer seatNumber, Character section, SeatType type, Boolean isAvailable){
		this.seatNumber = seatNumber;
		this.section = section;
		this.type = type;
		this.isAvailable = isAvailable;
	}

}
