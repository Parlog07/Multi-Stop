package com.multi.multistop.mapper;

import com.multi.multistop.dto.request.VehicleRequestDTO;
import com.multi.multistop.dto.response.VehicleResponseDTO;
import com.multi.multistop.entity.Vehicle;
import com.mapstruct.Mapper;
import com.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "trips", ignore = true)
    Vehicle toEntity(VehicleRequestDTO request);

    @Mapping(source = "driver.id", target = "driverId")
    VehicleResponseDTO toResponseDTO(Vehicle vehicle);
}

