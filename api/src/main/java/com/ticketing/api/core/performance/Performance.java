package com.ticketing.api.core.performance;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Performance {

	private Integer performanceId;

	private String title;

	private List<PricePolicy> pricePolicies;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime bookingStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime bookingEndDate;

	private List<Schedule> scheduleList;

	private String serviceAddress;

	public Performance(){}

	public Performance(Integer performanceId, String title, List<PricePolicy> pricePolicies,
		LocalDateTime bookingStartDate, LocalDateTime bookingEndDate, List<Schedule> scheduleList){
		this.performanceId = performanceId;
		this.title = title;
		this.pricePolicies = pricePolicies;
		this.bookingStartDate = bookingStartDate;
		this.bookingEndDate = bookingEndDate;
		this.scheduleList = scheduleList;
	}

	public Performance(Integer performanceId, String title, List<PricePolicy> pricePolicies,
		LocalDateTime bookingStartDate, LocalDateTime bookingEndDate, List<Schedule> scheduleList,
		String serviceAddress) {
		this.performanceId = performanceId;
		this.title = title;
		this.pricePolicies = pricePolicies;
		this.bookingStartDate = bookingStartDate;
		this.bookingEndDate = bookingEndDate;
		this.scheduleList = scheduleList;
		this.serviceAddress = serviceAddress;
	}

}
