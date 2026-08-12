package com.bestdriver.was.route.domain;

import java.util.Locale;

public enum VehicleType {
    TRUCK("트럭", 1.35),
    PASSENGER("승용", 0.80),
    SUV("SUV", 1.00);

    private final String displayName;
    private final double energyMultiplier;

    VehicleType(String displayName, double energyMultiplier) {
        this.displayName = displayName;
        this.energyMultiplier = energyMultiplier;
    }

    public String displayName() {
        return displayName;
    }

    public double energyMultiplier() {
        return energyMultiplier;
    }

    public static VehicleType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("차량 종류를 입력해주세요.");
        }

        return switch (value.trim().toUpperCase(Locale.ROOT)) {
            case "트럭", "TRUCK" -> TRUCK;
            case "승용", "승용차", "PASSENGER", "CAR" -> PASSENGER;
            case "SUV" -> SUV;
            default -> throw new IllegalArgumentException(
                    "지원하지 않는 차량 종류입니다: " + value);
        };
    }
}
