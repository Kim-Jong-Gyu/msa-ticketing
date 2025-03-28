package com.ticketing.api.core.performance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class PricePolicy{

	private String seatType;

	private Integer price;

	public PricePolicy(String seatType, Integer price) {
		this.seatType = seatType;
		this.price = price;
	}
}
