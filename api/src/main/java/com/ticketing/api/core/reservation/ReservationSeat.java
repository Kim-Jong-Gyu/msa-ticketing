package com.ticketing.api.core.reservation;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationSeat {

	private Integer performanceId;

	private Integer hallId;

	private LocalDateTime performanceDate;

	private Integer seatNumber;

	private Character section;

	private String serviceAddress;

	public ReservationSeat(Integer performanceId, Integer hallId, LocalDateTime performanceDate, Integer seatNumber,
		Character section, String serviceAddress) {
		this.performanceId = performanceId;
		this.hallId = hallId;
		this.performanceDate = performanceDate;
		this.seatNumber = seatNumber;
		this.section = section;
		this.serviceAddress = serviceAddress;
	}
}
