
package com.multi.multistop.service.imp;

import com.multi.multistop.dto.request.DriverRequestDTO;
import com.multi.multistop.dto.response.DriverResponseDTO;
import com.multi.multistop.entity.Driver;
import com.multi.multistop.mapper.DriverMapper;
import com.multi.multistop.repository.DriverRepository;
import com.multi.multistop.service.DriverService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Override
    public DriverResponseDTO create(DriverRequestDTO request) {

        if (driverRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (driverRepository.existsByLicenseNumber(request.licenseNumber())) {
            throw new IllegalArgumentException("License number already exists");
        }

        Driver driver = driverMapper.toEntity(request);

        Driver savedDriver = driverRepository.save(driver);

        return driverMapper.toResponseDTO(savedDriver);
    }

    @Override
    public DriverResponseDTO getById(Long id) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Driver not found")
                );

        return driverMapper.toResponseDTO(driver);
    }

    @Override
    public List<DriverResponseDTO> getAll() {

        return driverRepository.findAll()
                .stream()
                .map(driverMapper::toResponseDTO)
                .toList();
    }

    @Override
    public DriverResponseDTO update(Long id, DriverRequestDTO request) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Driver not found")
                );

        driver.setFirstName(request.firstName());
        driver.setLastName(request.lastName());
        driver.setEmail(request.email());
        driver.setPhone(request.phone());
        driver.setLicenseNumber(request.licenseNumber());

        Driver updatedDriver = driverRepository.save(driver);

        return driverMapper.toResponseDTO(updatedDriver);
    }

    @Override
    public void delete(Long id) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Driver not found")
                );

        driverRepository.delete(driver);
    }
}
