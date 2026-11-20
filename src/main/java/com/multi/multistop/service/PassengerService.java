
package com.multi.multistop.service;

import com.multi.multistop.dto.request.PassengerRequestDTO;
import com.multi.multistop.dto.response.PassengerResponseDTO;

import java.util.List;

public interface PassengerService {

    PassengerResponseDTO create(PassengerRequestDTO request);

    PassengerResponseDTO getById(Long id);

    List<PassengerResponseDTO> getAll();

    PassengerResponseDTO update(Long id, PassengerRequestDTO request);

    void delete(Long id);
}
