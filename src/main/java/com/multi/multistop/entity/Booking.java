package com.multi.multistop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.multi.enums.BookingStatus;

@Entity
@Table(name="bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private Integer numberOfSeats;

    @Column(nullable=false,precision=12,scale=2)
    private BigDecimal totalAmount;

    @Column(nullable=false)
    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private BookingStatus status;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="passenger_id",nullable=false)
    private Passenger passenger;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="trip_id",nullable=false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pick_up_point_id", nullable = false)
    private StoppingPoint pickUpPoint;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "drop_off_point_id", nullable = false)
    private StoppingPoint dropOffPoint;
}
