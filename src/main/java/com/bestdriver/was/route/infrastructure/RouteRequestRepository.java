package com.bestdriver.was.route.infrastructure;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bestdriver.was.route.domain.RouteRequest;

public interface RouteRequestRepository extends JpaRepository<RouteRequest, UUID> {

    @EntityGraph(attributePaths = "candidates")
    Optional<RouteRequest> findByOriginNameIgnoreCaseAndDestinationNameIgnoreCaseAndStatus(
            String originName,
            String destinationName,
            String status);
}
