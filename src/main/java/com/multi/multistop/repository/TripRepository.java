
package com.multi.multistop.repository;

import com.multi.multistop.entity.Trip;
import com.multi.multistop.enums.TripStatus;
import com.multi.multistop.enums.BookingStatus;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByDriverId(Long driverId);

    List<Trip> findByStatus(TripStatus status);

    boolean existsByVehicleId(Long vehicleId);

    @Query("""
        SELECT t FROM Trip t
        JOIN t.stoppingPoints pickup
        JOIN t.stoppingPoints dropoff
        WHERE LOWER(pickup.city) = LOWER(:departure)
          AND LOWER(dropoff.city) = LOWER(:destination)
          AND pickup.pointOrder < dropoff.pointOrder
          AND pickup.passingTime > CURRENT_TIMESTAMP
          AND t.status = :status
          AND (
              t.seatsOffered -
              (SELECT COALESCE(SUM(b.numberOfSeats), 0)
               FROM Booking b
               WHERE b.trip = t
                 AND b.status = :bookingStatus)
          ) >= :seats
        """)
    Page<Trip> searchTrips(
            @Param("departure") String departure,
            @Param("destination") String destination,
            @Param("seats") int seats,
            @Param("status") TripStatus status,
            @Param("bookingStatus") BookingStatus bookingStatus,
            Pageable pageable
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Trip t WHERE t.id = :id")
    Optional<Trip> findByIdForUpdate(@Param("id") Long id);

}
