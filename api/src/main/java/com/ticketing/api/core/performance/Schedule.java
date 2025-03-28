package com.ticketing.api.core.performance;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Schedule{

	Integer hallId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	LocalDateTime performanceDate;

	public Schedule(Integer hallId, LocalDateTime performanceDate) {
		this.hallId = hallId;
		this.performanceDate = performanceDate;
	}
}
