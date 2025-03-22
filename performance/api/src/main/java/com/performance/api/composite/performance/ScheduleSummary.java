package com.performance.api.composite.performance;

import java.time.LocalDateTime;

public record ScheduleSummary(

	LocalDateTime performanceDate,

	Integer hallId,

	String hallName

) {
}
