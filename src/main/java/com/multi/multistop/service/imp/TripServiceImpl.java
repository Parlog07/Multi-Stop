
package com.multi.multistop.service.imp;

import com.multi.multistop.dto.request.TripRequestDTO;
import com.multi.multistop.dto.request.StoppingPointRequestDTO;
import com.multi.multistop.dto.response.TripResponseDTO;
import com.multi.multistop.entity.Trip;
import com.multi.multistop.entity.Vehicle;
import com.multi.multistop.entity.StoppingPoint;
import com.multi.multistop.enums.BookingStatus;
import com.multi.multistop.enums.TripStatus;
import com.multi.multistop.mapper.TripMapper;
import com.multi.multistop.repository.TripRepository;
import com.multi.multistop.repository.VehicleRepository;
import com.multi.multistop.service.TripService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;
    private final TripMapper tripMapper;

    @Override
    @Transactional
    public TripResponseDTO create(TripRequestDTO request) {

        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Vehicle not found")
                );

        if (request.seatsOffered() < 1 ||
                request.seatsOffered() > vehicle.getCapacity()) {
            throw new IllegalArgumentException("Invalid number of seats");
        }

        if (request.stoppingPoints() == null ||
                request.stoppingPoints().size() < 2) {
            throw new IllegalArgumentException(
                    "A trip must have at least 2 stops"
            );
        }

        List<StoppingPointRequestDTO> stops =
                request.stoppingPoints().stream()
                        .sorted(Comparator.comparingInt(
                                StoppingPointRequestDTO::pointOrder
                        ))
                        .toList();

        Set<String> cities = new HashSet<>();

        for (int i = 0; i < stops.size(); i++) {

            StoppingPointRequestDTO stop = stops.get(i);

            if (stop.city() == null || stop.city().isBlank()) {
                throw new IllegalArgumentException("City is required");
            }

            if (!cities.add(stop.city().trim().toLowerCase())) {
                throw new IllegalArgumentException("Duplicate city");
            }

            if (stop.pointOrder() != i + 1) {
                throw new IllegalArgumentException(
                        "Stop order must start at 1 without gaps"
                );
            }

            if (stop.passingTime() == null ||
                    !stop.passingTime().isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException(
                        "Stop time must be in the future"
                );
            }

            if (stop.priceFromDeparture() == null ||
                    stop.priceFromDeparture().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Invalid price");
            }

            if (i == 0) {
                if (stop.priceFromDeparture().compareTo(BigDecimal.ZERO) != 0) {
                    throw new IllegalArgumentException(
                            "First stop price must be zero"
                    );
                }
            } else {

                StoppingPointRequestDTO previous = stops.get(i - 1);

                if (!stop.passingTime().isAfter(previous.passingTime())) {
                    throw new IllegalArgumentException(
                            "Stop times must increase"
                    );
                }

                if (stop.priceFromDeparture()
                        .compareTo(previous.priceFromDeparture()) <= 0) {
                    throw new IllegalArgumentException(
                            "Stop prices must increase"
                    );
                }
            }
        }

        Trip trip = tripMapper.toEntity(request);

        trip.setVehicle(vehicle);
        trip.setDriver(vehicle.getDriver());
        trip.setDepartureCity(stops.get(0).city());
        trip.setDestinationCity(stops.get(stops.size() - 1).city());
        trip.setDepartureDate(stops.get(0).passingTime());
        trip.setStatus(TripStatus.PUBLISHED);

        trip.getStoppingPoints().sort(
                Comparator.comparingInt(StoppingPoint::getPointOrder)
        );

        for (StoppingPoint point : trip.getStoppingPoints()) {
            point.setTrip(trip);
        }

        Trip saved = tripRepository.save(trip);

        return tripMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TripResponseDTO getById(Long id) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Trip not found")
                );

        return tripMapper.toResponseDTO(trip);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripResponseDTO> getAll() {

        return tripRepository.findAll()
                .stream()
                .map(tripMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripResponseDTO> getByDriverId(Long driverId) {

        return tripRepository.findByDriverId(driverId)
                .stream()
                .map(tripMapper::toResponseDTO)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public Page<TripResponseDTO> searchTrips(
            String departure,
            String destination,
            int seats,
            int page,
            int size) {

        if (departure == null || departure.isBlank()
                || destination == null || destination.isBlank()) {
            throw new IllegalArgumentException(
                    "Departure and destination are required"
            );
        }

        departure = departure.trim();
        destination = destination.trim();

        if (departure.equalsIgnoreCase(destination)) {
            throw new IllegalArgumentException(
                    "Departure and destination must be different"
            );
        }

        if (seats < 1) {
            throw new IllegalArgumentException(
                    "Seats must be at least 1"
            );
        }

        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Invalid pagination parameters"
            );
        }

        Pageable pageable = PageRequest.of(page, size);

        return tripRepository.searchTrips(
                departure,
                destination,
                seats,
                TripStatus.PUBLISHED,
                BookingStatus.CONFIRMED,
                pageable
        ).map(tripMapper::toResponseDTO);
    }

}
