
package com.multi.multistop.service;

import com.multi.multistop.dto.request.DriverRequestDTO;
import com.multi.multistop.dto.response.DriverResponseDTO;

import java.util.List;

public interface DriverService {

    DriverResponseDTO create(DriverRequestDTO request);

    DriverResponseDTO getById(Long id);

    List<DriverResponseDTO> getAll();

    DriverResponseDTO update(Long id, DriverRequestDTO request);

    void delete(Long id);
}
