
package com.multi.multistop.repository;

import com.multi.multistop.entity.Trip;
import com.multi.multistop.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByDriverId(Long driverId);

    List<Trip> findByStatus(TripStatus status);

    boolean existsByVehicleId(Long vehicleId);
}
