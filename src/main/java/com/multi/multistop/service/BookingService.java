
package com.multi.multistop.service;

import com.multi.multistop.dto.request.BookingRequestDTO;
import com.multi.multistop.dto.response.BookingResponseDTO;

import java.util.List;

public interface BookingService {

    BookingResponseDTO create(BookingRequestDTO request);

    BookingResponseDTO getById(Long id);

    List<BookingResponseDTO> getAll();

    List<BookingResponseDTO> getByPassengerId(Long passengerId);

    List<BookingResponseDTO> getByTripId(Long tripId);

    BookingResponseDTO cancel(Long id);
}
