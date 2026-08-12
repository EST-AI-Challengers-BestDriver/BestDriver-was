package com.bestdriver.was.prediction;

public interface EnergyPredictor {

    PredictionResult predict(RouteFeatures features);

    String mode();
}
