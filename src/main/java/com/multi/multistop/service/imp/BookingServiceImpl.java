
package com.multi.multistop.service.imp;

import com.multi.multistop.dto.request.BookingRequestDTO;
import com.multi.multistop.dto.response.BookingResponseDTO;
import com.multi.multistop.entity.Booking;
import com.multi.multistop.entity.Passenger;
import com.multi.multistop.entity.StoppingPoint;
import com.multi.multistop.entity.Trip;
import com.multi.multistop.enums.BookingStatus;
import com.multi.multistop.enums.TripStatus;
import com.multi.multistop.exception.BusinessException;
import com.multi.multistop.exception.ResourceNotFoundException;
import com.multi.multistop.mapper.BookingMapper;
import com.multi.multistop.repository.BookingRepository;
import com.multi.multistop.repository.PassengerRepository;
import com.multi.multistop.repository.StoppingPointRepository;
import com.multi.multistop.repository.TripRepository;
import com.multi.multistop.service.BookingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final TripRepository tripRepository;
    private final StoppingPointRepository stoppingPointRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponseDTO create(BookingRequestDTO request) {

        if (request.numberOfSeats() < 1) {
            throw new IllegalArgumentException(
                    "Number of seats must be at least 1"
            );
        }

        Passenger passenger = passengerRepository
                .findById(request.passengerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Passenger not found")
                );

        Trip trip = tripRepository.findByIdForUpdate(request.tripId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found")
                );

        if (trip.getStatus() != TripStatus.PUBLISHED) {
            throw new IllegalArgumentException(
                    "Trip is not available"
            );
        }

        StoppingPoint pickup = stoppingPointRepository
                .findById(request.pickUpPointId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pickup point not found")
                );

        StoppingPoint dropoff = stoppingPointRepository
                .findById(request.dropOffPointId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Drop-off point not found")
                );

        if (!pickup.getTrip().getId().equals(trip.getId())
                || !dropoff.getTrip().getId().equals(trip.getId())) {
            throw new IllegalArgumentException(
                    "Stopping points must belong to the selected trip"
            );
        }

        if (pickup.getPointOrder() >= dropoff.getPointOrder()) {
            throw new IllegalArgumentException(
                    "Pickup must come before drop-off"
            );
        }

        if (!pickup.getPassingTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Pickup time must be in the future"
            );
        }

        int reservedSeats = bookingRepository
                .findByTripIdAndStatus(
                        trip.getId(),
                        BookingStatus.CONFIRMED
                )
                .stream()
                .mapToInt(Booking::getNumberOfSeats)
                .sum();

        int remainingSeats = trip.getSeatsOffered() - reservedSeats;

        if (request.numberOfSeats() > remainingSeats) {
            throw new BusinessException(
                    "Not enough available seats"
            );
        }

        BigDecimal pricePerSeat = dropoff.getPriceFromDeparture()
                .subtract(pickup.getPriceFromDeparture());

        if (pricePerSeat.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Invalid booking price"
            );
        }

        BigDecimal totalAmount = pricePerSeat.multiply(
                BigDecimal.valueOf(request.numberOfSeats())
        );

        Booking booking = bookingMapper.toEntity(request);

        booking.setPassenger(passenger);
        booking.setTrip(trip);
        booking.setPickUpPoint(pickup);
        booking.setDropOffPoint(dropoff);
        booking.setTotalAmount(totalAmount);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking saved = bookingRepository.save(booking);

        return bookingMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDTO getById(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Booking not found")
                );

        return bookingMapper.toResponseDTO(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAll() {

        return bookingRepository.findAll()
                .stream()
                .map(bookingMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getByPassengerId(Long passengerId) {

        return bookingRepository.findByPassengerId(passengerId)
                .stream()
                .map(bookingMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getByTripId(Long tripId) {

        return bookingRepository.findByTripId(tripId)
                .stream()
                .map(bookingMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public BookingResponseDTO cancel(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Booking not found")
                );

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Booking is already cancelled"
            );
        }

        LocalDateTime deadline = booking.getPickUpPoint()
                .getPassingTime()
                .minusHours(24);

        if (!LocalDateTime.now().isBefore(deadline)) {
            throw new IllegalArgumentException(
                    "Cancellation must be at least 24 hours before pickup"
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);

        return bookingMapper.toResponseDTO(
                bookingRepository.save(booking)
        );
    }
}
