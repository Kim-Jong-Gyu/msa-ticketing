package com.ticketing.storage.core.reservation.persistence;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservedLineVo {

	private Integer performanceId;

	private Integer hallId;

	private LocalDateTime performanceDate;

	private Integer seatNumber;

	private Character section;

	private Integer price;

	public ReservedLineVo(Integer performanceId, Integer hallId, LocalDateTime performanceDate, Integer seatNumber,
		Character section, Integer price) {
		this.performanceId = performanceId;
		this.hallId = hallId;
		this.performanceDate = performanceDate;
		this.seatNumber = seatNumber;
		this.section = section;
		this.price = price;
	}
}
