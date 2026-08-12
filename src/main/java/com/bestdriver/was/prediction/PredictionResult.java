package com.bestdriver.was.prediction;

public record PredictionResult(
        double energyConsumption,
        String energyUnit,
        double co2Kg) {
}
