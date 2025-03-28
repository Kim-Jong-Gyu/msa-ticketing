package com.ticketing.storage.core.hall.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "halls")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HallEntity {

	@Id
	@GeneratedValue
	private Integer id;

	@Version
	private Integer version;

	@Column(unique = true)
	private Integer hallId;

	private String hallName;

	@ElementCollection
	@CollectionTable(name = "unavailable_date", joinColumns = @JoinColumn(name = "hall_id"))
	private List<LocalDateTime> unavailableDateList = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "seat", joinColumns = @JoinColumn(name = "hall_id"))
	private List<SeatVO> seatList = new ArrayList<>();

	public HallEntity(Integer hallId, String hallName, List<SeatVO> seatList, List<LocalDateTime> unavailableDateList) {
		this.hallId = hallId;
		this.hallName = hallName;
		this.seatList = seatList;
		if(unavailableDateList.isEmpty())
			this.unavailableDateList = new ArrayList<>();
		else
			this.unavailableDateList = unavailableDateList;
	}

	public void updateName(String hallName) {
		this.hallName = hallName;
	}
}
