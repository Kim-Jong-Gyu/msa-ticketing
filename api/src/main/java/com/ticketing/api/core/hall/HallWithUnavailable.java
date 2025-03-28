package com.ticketing.api.core.hall;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HallWithUnavailable {

	private Integer hallId;
	private String hallName;
	private List<LocalDateTime> unavailableDateList;
	private String serviceAddress;

	public HallWithUnavailable(Integer hallId, String hallName, List<LocalDateTime> unavailableDateList, String serviceAddress) {
		this.hallId = hallId;
		this.hallName = hallName;
		this.unavailableDateList = unavailableDateList;
		this.serviceAddress = serviceAddress;
	}
}
