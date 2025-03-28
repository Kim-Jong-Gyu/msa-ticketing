package com.ticketing.api.core.reservation;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservedLine {

	private Integer performanceId;

	private Integer hallId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime performanceDate;

	private Integer seatNumber;

	private Character section;

	private Integer price;

	public ReservedLine(){}

	public ReservedLine(Integer performanceId, Integer hallId, LocalDateTime performanceDate, Integer seatNumber, Character section,
		Integer price) {
		this.performanceId = performanceId;
		this.hallId = hallId;
		this.performanceDate = performanceDate;
		this.seatNumber = seatNumber;
		this.section = section;
		this.price = price;
	}
}
