package com.multi.multistop.mapper;

import com.multi.multistop.dto.request.PassengerRequestDTO;
import com.multi.multistop.dto.response.PassengerResponseDTO;
import com.multi.multistop.entity.Passenger;
import com.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    Passenger toEntity(PassengerRequestDTO request);

    PassengerResponseDTO toResponseDTO(Passenger passenger);
}

