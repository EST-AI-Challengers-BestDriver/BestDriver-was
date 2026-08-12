package com.bestdriver.was.route.application;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bestdriver.was.prediction.EnergyPredictor;
import com.bestdriver.was.prediction.PredictionResult;
import com.bestdriver.was.prediction.RouteFeatures;
import com.bestdriver.was.route.domain.RouteCandidate;
import com.bestdriver.was.route.domain.RouteRequest;
import com.bestdriver.was.route.domain.VehicleType;
import com.bestdriver.was.route.infrastructure.RouteRequestRepository;
import com.bestdriver.was.route.presentation.RouteAnalysisRequest;
import com.bestdriver.was.route.presentation.RouteAnalysisResponse;
import com.bestdriver.was.route.presentation.RouteOptionResponse;

@Service
public class RouteAnalysisService {

    private static final String TEMPLATE_STATUS = "TEMPLATE";

    private final RouteRequestRepository routeRequestRepository;
    private final EnergyPredictor energyPredictor;

    public RouteAnalysisService(
            RouteRequestRepository routeRequestRepository,
            EnergyPredictor energyPredictor) {
        this.routeRequestRepository = routeRequestRepository;
        this.energyPredictor = energyPredictor;
    }

    @Transactional(readOnly = true)
    public RouteAnalysisResponse analyze(RouteAnalysisRequest request) {
        VehicleType vehicleType = VehicleType.from(request.vehicleType());
        String normalizedStart = normalizeLocation(request.start());
        String normalizedDestination = normalizeLocation(request.destination());

        RouteRequest template = routeRequestRepository
                .findByOriginNameIgnoreCaseAndDestinationNameIgnoreCaseAndStatus(
                        normalizedStart,
                        normalizedDestination,
                        TEMPLATE_STATUS)
                .orElseThrow(() -> new RouteRequestNotFoundException(
                        request.start().trim(),
                        request.destination().trim()));

        List<PredictedCandidate> predictions = template.getCandidates().stream()
                .map(candidate -> predict(candidate, vehicleType))
                .toList();

        PredictedCandidate recommended = predictions.stream()
                .min(Comparator.comparingDouble(candidate -> candidate.prediction().co2Kg()))
                .orElseThrow(() -> new IllegalStateException("후보 경로가 없습니다."));
        PredictedCandidate baseline = predictions.stream()
                .filter(candidate -> candidate.candidate().isBaseline())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("기준 경로가 없습니다."));

        List<RouteOptionResponse> routes = predictions.stream()
                .map(candidate -> toResponse(
                        candidate,
                        candidate.candidate().getId().equals(recommended.candidate().getId())))
                .toList();

        double savedEnergy = Math.max(
                0,
                baseline.prediction().energyConsumption()
                        - recommended.prediction().energyConsumption());
        double savedEnergyPercent = baseline.prediction().energyConsumption() > 0
                ? savedEnergy / baseline.prediction().energyConsumption() * 100
                : 0;

        return new RouteAnalysisResponse(
                UUID.randomUUID().toString(),
                request.start().trim(),
                request.destination().trim(),
                vehicleType.displayName(),
                energyPredictor.mode(),
                routeId(recommended.candidate()),
                routeId(baseline.candidate()),
                routes,
                round(savedEnergyPercent, 1),
                round(savedEnergy, 1));
    }

    private PredictedCandidate predict(RouteCandidate candidate, VehicleType vehicleType) {
        RouteFeatures features = new RouteFeatures(
                candidate.getDistanceKm(),
                candidate.getDurationMinutes(),
                candidate.getAverageSpeedKmh(),
                vehicleType);
        return new PredictedCandidate(candidate, energyPredictor.predict(features));
    }

    private RouteOptionResponse toResponse(
            PredictedCandidate predicted,
            boolean recommended) {
        RouteCandidate candidate = predicted.candidate();
        PredictionResult prediction = predicted.prediction();
        return new RouteOptionResponse(
                routeId(candidate),
                candidate.getName(),
                candidate.getDistanceKm(),
                candidate.getDurationMinutes(),
                candidate.getTollFare(),
                candidate.isBaseline(),
                prediction.energyConsumption(),
                prediction.energyUnit(),
                prediction.co2Kg(),
                recommended);
    }

    private String routeId(RouteCandidate candidate) {
        return candidate.getId().toString();
    }

    private String normalizeLocation(String value) {
        String normalized = value.trim().replaceAll("\\s+", "");
        if (normalized.endsWith("역") && normalized.length() > 1) {
            return normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private double round(double value, int scale) {
        double factor = Math.pow(10, scale);
        return Math.round(value * factor) / factor;
    }

    private record PredictedCandidate(
            RouteCandidate candidate,
            PredictionResult prediction) {
    }
}
