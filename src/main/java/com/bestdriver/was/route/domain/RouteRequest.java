package com.bestdriver.was.route.domain;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "route_request")
public class RouteRequest {

    @Id
    @Column(name = "route_request_id", nullable = false)
    private UUID id;

    @Column(name = "origin_name", nullable = false)
    private String originName;

    @Column(name = "destination_name", nullable = false)
    private String destinationName;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "routeRequest")
    @OrderBy("createdAt ASC")
    private List<RouteCandidate> candidates = new ArrayList<>();

    protected RouteRequest() {
    }

    public UUID getId() {
        return id;
    }

    public String getOriginName() {
        return originName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getStatus() {
        return status;
    }

    public List<RouteCandidate> getCandidates() {
        return List.copyOf(candidates);
    }
}
