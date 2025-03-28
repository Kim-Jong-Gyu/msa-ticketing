package com.ticketing.api.core.hall;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HallWithSeat {

	private Integer hallId;
	private String hallName;
	private List<Seat> seatList;
	private String serviceAddress;

	public HallWithSeat(Integer hallId, String hallName, List<Seat> seatList, String serviceAddress) {
		this.hallId = hallId;
		this.hallName = hallName;
		this.seatList = seatList;
		this.serviceAddress = serviceAddress;
	}
}
