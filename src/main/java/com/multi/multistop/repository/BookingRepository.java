
package com.multi.multistop.repository;

import com.multi.multistop.entity.Booking;
import com.multi.multistop.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByPassengerId(Long passengerId);

    List<Booking> findByTripId(Long tripId);

    boolean existsByPassengerIdAndTripIdAndStatus(
            Long passengerId,
            Long tripId,
            BookingStatus status
    );
}
