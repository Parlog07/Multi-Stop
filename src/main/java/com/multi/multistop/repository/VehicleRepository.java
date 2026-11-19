
package com.multi.multistop.repository;

import com.multi.multistop.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByDriverId(Long driverId);

    boolean existsByRegistrationNumber(String registrationNumber);

    boolean existsByDriverIdAndId(Long driverId, Long vehicleId);
}
