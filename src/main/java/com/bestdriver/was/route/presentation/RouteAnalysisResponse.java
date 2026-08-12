package com.bestdriver.was.route.presentation;

import java.util.List;

public record RouteAnalysisResponse(
        String requestId,
        String start,
        String destination,
        String vehicleType,
        String predictionMode,
        String recommendedRouteId,
        String baselineRouteId,
        List<RouteOptionResponse> routes,
        double savedEnergyPercent,
        double savedEnergy) {
}
