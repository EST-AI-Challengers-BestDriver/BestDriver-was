package com.bestdriver.was.prediction;

import com.bestdriver.was.route.domain.VehicleType;

public record RouteFeatures(
        double distanceKm,
        int durationMinutes,
        double averageSpeedKmh,
        VehicleType vehicleType) {
}
