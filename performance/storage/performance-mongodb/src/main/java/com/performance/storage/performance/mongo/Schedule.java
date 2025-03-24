package com.performance.storage.performance.mongo;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

	private Integer hallId;

	private LocalDateTime performanceDate;

}
