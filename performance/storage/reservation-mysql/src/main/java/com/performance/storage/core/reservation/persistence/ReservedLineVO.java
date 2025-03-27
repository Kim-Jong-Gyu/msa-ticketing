package com.performance.storage.core.reservation.persistence;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservedLineVO {

	private Integer hallId;

	private String hallName;

	LocalDateTime performanceDate;

	private Integer seatNumber;

	private Character section;

	private Integer price;

	public ReservedLineVO(Integer hallId, String hallName, LocalDateTime performanceDate, Integer seatNumber,
		Character section, Integer price) {
		this.hallId = hallId;
		this.hallName = hallName;
		this.performanceDate = performanceDate;
		this.seatNumber = seatNumber;
		this.section = section;
		this.price = price;
	}
}
