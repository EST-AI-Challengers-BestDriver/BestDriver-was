package com.bestdriver.was.prediction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        prefix = "bestdriver.prediction",
        name = "mode",
        havingValue = "mock",
        matchIfMissing = true)
public class MockEnergyPredictor implements EnergyPredictor {

    private static final Logger log = LoggerFactory.getLogger(MockEnergyPredictor.class);
    private static final double MOCK_CO2_FACTOR = 0.22;

    public MockEnergyPredictor() {
        log.warn("Mock energy predictor is active. Results are demonstration estimates, not ONNX inference.");
    }

    @Override
    public PredictionResult predict(RouteFeatures features) {
        double speedPenalty = Math.max(0, features.averageSpeedKmh() - 70) * 0.0015;
        double roadFactor = 0.10 + speedPenalty;
        double energy = features.distanceKm()
                * features.vehicleType().energyMultiplier()
                * roadFactor;
        double co2 = energy * MOCK_CO2_FACTOR;

        return new PredictionResult(round(energy, 1), "kWh", round(co2, 2));
    }

    @Override
    public String mode() {
        return "MOCK";
    }

    private double round(double value, int scale) {
        double factor = Math.pow(10, scale);
        return Math.round(value * factor) / factor;
    }
}
