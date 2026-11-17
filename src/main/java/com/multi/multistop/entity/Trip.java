package com.multi.multistop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.multi.multistop.enums.BookingStatus;
import com.multi.multistop.enums.TripStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String departureCity;

    @Column(nullable = false)
    private String destinationCity;

    @Column(nullable = false)
    private LocalDateTime departureDate;

    @Column(nullable = false)
    private int seatsOffered;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @OneToMany(
            mappedBy = "trip",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("pointOrder ASC")
    private List<StoppingPoint> stoppingPoints = new ArrayList<>();

    @OneToMany(mappedBy = "trip")
    private List<Booking> bookings = new ArrayList<>();

    public int getRemainingSeats() {

        int bookedSeats = bookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.CONFIRMED)
                .mapToInt(Booking::getNumberOfSeats)
                .sum();

        return seatsOffered - bookedSeats;
    }
}
