package com.ticketing.api.core.reservation;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reservation {

	private Integer reservationId;

	private Integer userId;

	private List<ReservedLine> reservedLineList;

	private String reservationStatus;

	private String serviceAddress;

	public Reservation(){}

	public Reservation(Integer reservationId, Integer userId, List<ReservedLine> reservedLineList,
		String reservationStatus) {
		this.reservationId = reservationId;
		this.userId = userId;
		this.reservedLineList = reservedLineList;
		this.reservationStatus = reservationStatus;
	}
}
