package com.bestdriver.was.route.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "route_candidate")
public class RouteCandidate {

    @Id
    @Column(name = "route_candidate_id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_request_id", nullable = false)
    private RouteRequest routeRequest;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "provider_option", nullable = false)
    private String providerOption;

    @Column(name = "distance_m", nullable = false)
    private int distanceM;

    @Column(name = "duration_sec", nullable = false)
    private int durationSec;

    @Column(name = "toll_fare")
    private Integer tollFare;

    @Column(name = "is_baseline", nullable = false)
    private boolean baseline;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected RouteCandidate() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return providerOption;
    }

    public double getDistanceKm() {
        return distanceM / 1000.0;
    }

    public int getDurationMinutes() {
        return (int) Math.round(durationSec / 60.0);
    }

    public double getAverageSpeedKmh() {
        return durationSec > 0 ? getDistanceKm() / (durationSec / 3600.0) : 0;
    }

    public Long getTollFare() {
        return tollFare == null ? null : tollFare.longValue();
    }

    public boolean isBaseline() {
        return baseline;
    }
}
