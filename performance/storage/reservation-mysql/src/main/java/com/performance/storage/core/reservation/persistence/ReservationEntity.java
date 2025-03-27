package com.performance.storage.core.reservation.persistence;

import java.util.List;

import com.performance.common.ReservationStatus;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationEntity {

	@Id
	@GeneratedValue
	private Integer id;

	@Version
	private Integer version;

	@Column(unique = true)
	private Integer reservationId;

	private Integer userId;

	private Integer performanceId;

	@ElementCollection()
	@CollectionTable(name = "reserved_line", joinColumns = @JoinColumn(name = "reservation_id"))
	private List<ReservedLineVO> reservedLineList;

	@Enumerated(EnumType.STRING)
	private ReservationStatus reservationStatus;

	public ReservationEntity(Integer reservationId, Integer userId, Integer performanceId,
		List<ReservedLineVO> reservedLineList, ReservationStatus reservationStatus) {
		this.reservationId = reservationId;
		this.userId = userId;
		this.performanceId = performanceId;
		this.reservedLineList = reservedLineList;
		this.reservationStatus = reservationStatus;
	}

	public void updateUser(Integer userId) {
		this.userId = userId;
	}
}
