
package com.multi.multistop.service.imp;

import com.multi.multistop.dto.request.PassengerRequestDTO;
import com.multi.multistop.dto.response.PassengerResponseDTO;
import com.multi.multistop.entity.Passenger;
import com.multi.multistop.mapper.PassengerMapper;
import com.multi.multistop.repository.PassengerRepository;
import com.multi.multistop.service.PassengerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    @Transactional
    public PassengerResponseDTO create(PassengerRequestDTO request) {

        if (passengerRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Passenger passenger = passengerMapper.toEntity(request);
        Passenger saved = passengerRepository.save(passenger);

        return passengerMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PassengerResponseDTO getById(Long id) {

        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Passenger not found")
                );

        return passengerMapper.toResponseDTO(passenger);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PassengerResponseDTO> getAll() {

        return passengerRepository.findAll()
                .stream()
                .map(passengerMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public PassengerResponseDTO update(Long id, PassengerRequestDTO request) {

        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Passenger not found")
                );

        if (!passenger.getEmail().equals(request.email())
                && passengerRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        passenger.setFirstName(request.firstName());
        passenger.setLastName(request.lastName());
        passenger.setEmail(request.email());
        passenger.setPhone(request.phone());

        return passengerMapper.toResponseDTO(
                passengerRepository.save(passenger)
        );
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Passenger not found")
                );

        if (!passenger.getBookings().isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot delete a passenger with bookings"
            );
        }

        passengerRepository.delete(passenger);
    }
}
