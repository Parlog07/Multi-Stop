package com.multi.multistop.mapper;

import com.multi.multistop.dto.request.DriverRequestDTO;
import com.multi.multistop.dto.response.DriverResponseDTO;
import com.multi.multistop.entity.Driver;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    Driver toEntity(DriverRequestDTO request);

    DriverResponseDTO toResponseDTO(Driver driver);
}

