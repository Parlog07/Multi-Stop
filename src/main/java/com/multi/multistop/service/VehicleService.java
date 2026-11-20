
package com.multi.multistop.service;

import com.multi.multistop.dto.request.VehicleRequestDTO;
import com.multi.multistop.dto.response.VehicleResponseDTO;

import java.util.List;

public interface VehicleService {

    VehicleResponseDTO create(VehicleRequestDTO request);

    VehicleResponseDTO getById(Long id);

    List<VehicleResponseDTO> getAll();

    List<VehicleResponseDTO> getByDriverId(Long driverId);

    VehicleResponseDTO update(Long id, VehicleRequestDTO request);

    void delete(Long id);
}
