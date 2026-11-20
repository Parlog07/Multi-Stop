
package com.multi.multistop.service.imp;

import com.multi.multistop.dto.request.VehicleRequestDTO;
import com.multi.multistop.dto.response.VehicleResponseDTO;
import com.multi.multistop.entity.Driver;
import com.multi.multistop.entity.Vehicle;
import com.multi.multistop.mapper.VehicleMapper;
import com.multi.multistop.repository.DriverRepository;
import com.multi.multistop.repository.VehicleRepository;
import com.multi.multistop.repository.TripRepository;
import com.multi.multistop.service.VehicleService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final TripRepository tripRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    @Transactional
    public VehicleResponseDTO create(VehicleRequestDTO request) {

        if (request.capacity() < 1 || request.capacity() > 8) {
            throw new IllegalArgumentException("Capacity must be between 1 and 8");
        }

        if (vehicleRepository.existsByRegistrationNumber(request.registrationNumber())) {
            throw new IllegalArgumentException("Registration number already exists");
        }

        Driver driver = driverRepository.findById(request.driverId())
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));

        Vehicle vehicle = vehicleMapper.toEntity(request);
        vehicle.setDriver(driver);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponseDTO(savedVehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponseDTO getById(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        return vehicleMapper.toResponseDTO(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponseDTO> getAll() {

        return vehicleRepository.findAll()
                .stream()
                .map(vehicleMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponseDTO> getByDriverId(Long driverId) {

        return vehicleRepository.findByDriverId(driverId)
                .stream()
                .map(vehicleMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public VehicleResponseDTO update(Long id, VehicleRequestDTO request) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        if (request.capacity() < 1 || request.capacity() > 8) {
            throw new IllegalArgumentException("Capacity must be between 1 and 8");
        }

        if (!vehicle.getRegistrationNumber().equals(request.registrationNumber())
                && vehicleRepository.existsByRegistrationNumber(request.registrationNumber())) {
            throw new IllegalArgumentException("Registration number already exists");
        }

        Driver driver = driverRepository.findById(request.driverId())
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));

        vehicle.setMake(request.make());
        vehicle.setModel(request.model());
        vehicle.setRegistrationNumber(request.registrationNumber());
        vehicle.setColor(request.color());
        vehicle.setCapacity(request.capacity());
        vehicle.setDriver(driver);

        return vehicleMapper.toResponseDTO(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        if (tripRepository.existsByVehicleId(id)) {
            throw new IllegalArgumentException("Cannot delete a vehicle assigned to a trip");
        }

        vehicleRepository.delete(vehicle);
    }
}
