package com.ticketing.microservices.core.hall.services;

import com.ticketing.api.core.hall.Hall;
import com.ticketing.api.core.hall.HallWithSeat;
import com.ticketing.api.core.hall.HallWithUnavailable;
import com.ticketing.api.core.hall.Seat;
import com.ticketing.common.SeatType;
import com.ticketing.storage.core.hall.persistence.HallEntity;
import com.ticketing.storage.core.hall.persistence.SeatVO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-11T02:18:16+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class HallMapperImpl implements HallMapper {

    @Override
    public HallWithSeat entityToHallWithSeat(HallEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Integer hallId = null;
        String hallName = null;
        List<Seat> seatList = null;

        hallId = entity.getHallId();
        hallName = entity.getHallName();
        seatList = seatVOListToSeatList( entity.getSeatList() );

        String serviceAddress = null;

        HallWithSeat hallWithSeat = new HallWithSeat( hallId, hallName, seatList, serviceAddress );

        return hallWithSeat;
    }

    @Override
    public HallWithUnavailable entityToHallWithUnavailable(HallEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Integer hallId = null;
        String hallName = null;
        List<LocalDateTime> unavailableDateList = null;

        hallId = entity.getHallId();
        hallName = entity.getHallName();
        List<LocalDateTime> list = entity.getUnavailableDateList();
        if ( list != null ) {
            unavailableDateList = new ArrayList<LocalDateTime>( list );
        }

        String serviceAddress = null;

        HallWithUnavailable hallWithUnavailable = new HallWithUnavailable( hallId, hallName, unavailableDateList, serviceAddress );

        return hallWithUnavailable;
    }

    @Override
    public Hall entityToApi(HallEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Integer hallId = null;
        String hallName = null;
        List<Seat> seatList = null;
        List<LocalDateTime> unavailableDateList = null;

        hallId = entity.getHallId();
        hallName = entity.getHallName();
        seatList = seatVOListToSeatList( entity.getSeatList() );
        List<LocalDateTime> list1 = entity.getUnavailableDateList();
        if ( list1 != null ) {
            unavailableDateList = new ArrayList<LocalDateTime>( list1 );
        }

        Hall hall = new Hall( hallId, hallName, seatList, unavailableDateList );

        return hall;
    }

    @Override
    public HallEntity apiToEntity(Hall hall) {
        if ( hall == null ) {
            return null;
        }

        List<LocalDateTime> unavailableDateList = null;
        List<SeatVO> seatList = null;
        Integer hallId = null;
        String hallName = null;

        List<LocalDateTime> list = hall.getUnavailableDateList();
        if ( list != null ) {
            unavailableDateList = new ArrayList<LocalDateTime>( list );
        }
        seatList = seatListToSeatVOList( hall.getSeatList() );
        hallId = hall.getHallId();
        hallName = hall.getHallName();

        HallEntity hallEntity = new HallEntity( hallId, hallName, seatList, unavailableDateList );

        return hallEntity;
    }

    protected Seat seatVOToSeat(SeatVO seatVO) {
        if ( seatVO == null ) {
            return null;
        }

        Integer seatNumber = null;
        Character section = null;
        String type = null;

        seatNumber = seatVO.getSeatNumber();
        section = seatVO.getSection();
        if ( seatVO.getType() != null ) {
            type = seatVO.getType().name();
        }

        Seat seat = new Seat( seatNumber, section, type );

        return seat;
    }

    protected List<Seat> seatVOListToSeatList(List<SeatVO> list) {
        if ( list == null ) {
            return null;
        }

        List<Seat> list1 = new ArrayList<Seat>( list.size() );
        for ( SeatVO seatVO : list ) {
            list1.add( seatVOToSeat( seatVO ) );
        }

        return list1;
    }

    protected SeatVO seatToSeatVO(Seat seat) {
        if ( seat == null ) {
            return null;
        }

        Integer seatNumber = null;
        Character section = null;
        SeatType type = null;

        seatNumber = seat.getSeatNumber();
        section = seat.getSection();
        if ( seat.getType() != null ) {
            type = Enum.valueOf( SeatType.class, seat.getType() );
        }

        SeatVO seatVO = new SeatVO( seatNumber, section, type );

        return seatVO;
    }

    protected List<SeatVO> seatListToSeatVOList(List<Seat> list) {
        if ( list == null ) {
            return null;
        }

        List<SeatVO> list1 = new ArrayList<SeatVO>( list.size() );
        for ( Seat seat : list ) {
            list1.add( seatToSeatVO( seat ) );
        }

        return list1;
    }
}
