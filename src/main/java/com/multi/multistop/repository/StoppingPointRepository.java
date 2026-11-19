
package com.multi.multistop.repository;

import com.multi.multistop.entity.StoppingPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoppingPointRepository extends JpaRepository<StoppingPoint, Long> {

    List<StoppingPoint> findByTripIdOrderByPointOrderAsc(Long tripId);
}
