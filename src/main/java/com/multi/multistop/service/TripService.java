
package com.multi.multistop.service;

import com.multi.multistop.dto.request.TripRequestDTO;
import com.multi.multistop.dto.response.TripResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TripService {

    TripResponseDTO create(TripRequestDTO request);

    TripResponseDTO getById(Long id);

    List<TripResponseDTO> getAll();

    List<TripResponseDTO> getByDriverId(Long driverId);

    Page<TripResponseDTO> searchTrips(
            String departure,
            String destination,
            int seats,
            int page,
            int size
    );
}
