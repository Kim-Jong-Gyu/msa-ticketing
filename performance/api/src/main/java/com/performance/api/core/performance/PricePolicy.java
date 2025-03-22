package com.performance.api.core.performance;

import com.performance.api.enums.SeatType;

public record PricePolicy(

	SeatType seatType,

	Integer price

) {
}
