package com.multi.multistop.mapper;

import com.multi.multistop.dto.request.TripRequestDTO;
import com.multi.multistop.dto.response.TripResponseDTO;
import com.multi.multistop.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = StoppingPointMapper.class
)
public interface TripMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departureCity", ignore = true)
    @Mapping(target = "destinationCity", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(source = "stoppingPoints", target = "stoppingPoints")
    Trip toEntity(TripRequestDTO request);

    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "remainingSeats", target = "remainingSeats")
    TripResponseDTO toResponseDTO(Trip trip);
}

