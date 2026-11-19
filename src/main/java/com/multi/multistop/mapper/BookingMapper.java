package com.multi.multistop.mapper;

import com.multi.multistop.dto.request.BookingRequestDTO;
import com.multi.multistop.dto.response.BookingResponseDTO;
import com.multi.multistop.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passenger", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "pickUpPoint", ignore = true)
    @Mapping(target = "dropOffPoint", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "bookingDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Booking toEntity(BookingRequestDTO request);

    @Mapping(source = "passenger.id", target = "passengerId")
    @Mapping(source = "trip.id", target = "tripId")
    @Mapping(source = "pickUpPoint.id", target = "pickUpPointId")
    @Mapping(source = "dropOffPoint.id", target = "dropOffPointId")
    BookingResponseDTO toResponseDTO(Booking booking);
}

