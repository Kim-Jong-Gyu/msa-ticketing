package com.ticketing.api.core.hall;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hall{

	private Integer hallId;
	private String hallName;
	private List<Seat> seatList;
	private List<LocalDateTime> unavailableDateList;
	private String serviceAddress;

	public Hall(Integer hallId, String hallName, List<Seat> seatList, List<LocalDateTime> unavailableDateList){
		this.hallId = hallId;
		this.hallName = hallName;
		this.seatList = seatList;
		this.unavailableDateList = unavailableDateList;
	}
}
