package com.performance.microservices.core.performance.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.performance.api.core.type.SeatType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Document(collection = "performances")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceEntity {

	@Id
	private String id;

	@Version
	private Integer version;

	@Indexed(unique = true)
	private Integer performanceId;

	private String title;

	private Map<SeatType, Integer> pricePolicies;

	private LocalDateTime bookingStartDate;

	private LocalDateTime bookingEndDate;

	private List<Schedule> scheduleList;

	public PerformanceEntity(Integer performanceId, String title, Map<SeatType, Integer> pricePolicies,
		LocalDateTime bookingStartDate, LocalDateTime bookingEndDate, List<Schedule> scheduleList) {
		this.performanceId = performanceId;
		this.title = title;
		this.pricePolicies = pricePolicies;
		this.bookingStartDate = bookingStartDate;
		this.bookingEndDate = bookingEndDate;
		this.scheduleList = scheduleList;
	}

	public void updateTitle(String title) {
		this.title = title;
	}

}
