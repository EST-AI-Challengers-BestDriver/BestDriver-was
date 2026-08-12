package com.bestdriver.was.route.presentation;

public record RouteOptionResponse(
        String id,
        String name,
        double distanceKm,
        int durationMinutes,
        Long tollFare,
        boolean isBaseline,
        double energyConsumption,
        String energyUnit,
        double co2Kg,
        boolean isRecommended) {
}
