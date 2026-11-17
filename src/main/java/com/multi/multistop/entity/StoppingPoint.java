package com.multi.multistop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "stopping_points",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"trip_id", "point_order"}
                ),
                @UniqueConstraint(
                        columnNames = {"trip_id", "city"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class StoppingPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(name = "point_order", nullable = false)
    private int pointOrder;

    @Column(nullable = false)
    private LocalDateTime passingTime;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceFromDeparture;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;
}
