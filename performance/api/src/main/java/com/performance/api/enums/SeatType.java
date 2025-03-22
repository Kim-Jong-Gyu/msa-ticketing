package com.performance.api.enums;

public enum SeatType {

	VIP("VIP"), STANDARD("STANDARD");

	private final String value;

	SeatType(String value) {
		this.value = value;
	}

	public String getSeatType() {
		return value;
	}
}
