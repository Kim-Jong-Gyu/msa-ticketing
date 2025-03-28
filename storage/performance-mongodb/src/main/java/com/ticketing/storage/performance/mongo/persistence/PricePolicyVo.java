package com.ticketing.storage.performance.mongo.persistence;

import com.ticketing.common.SeatType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PricePolicyVo {

	private SeatType seatType;

	private Integer price;

	public PricePolicyVo(SeatType seatType, Integer price) {
		this.seatType = seatType;
		this.price = price;
	}
}
