package com.performance.api.core.performance;

import java.time.LocalDateTime;

public record Schedule(

	Integer hallId,

	LocalDateTime performanceDate
) {

}
