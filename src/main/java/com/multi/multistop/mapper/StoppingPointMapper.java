package com.multi.multistop.mapper;

import com.multi.multistop.dto.request.StoppingPointRequestDTO;
import com.multi.multistop.dto.response.StoppingPointResponseDTO;
import com.multi.multistop.entity.StoppingPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface StoppingPointMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trip", ignore = true)
    StoppingPoint toEntity(StoppingPointRequestDTO request);

    StoppingPointResponseDTO toResponseDTO(
            StoppingPoint stoppingPoint
    );
}

