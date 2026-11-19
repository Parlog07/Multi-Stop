package com.multi.multistop.repository;

import com.multi.multistop.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByLicenseNumber(String licenseNumber);
}
