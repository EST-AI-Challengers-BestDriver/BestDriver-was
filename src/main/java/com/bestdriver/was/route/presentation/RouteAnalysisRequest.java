package com.bestdriver.was.route.presentation;

import jakarta.validation.constraints.NotBlank;

public record RouteAnalysisRequest(
        @NotBlank(message = "출발지를 입력해주세요.") String start,
        @NotBlank(message = "목적지를 입력해주세요.") String destination,
        @NotBlank(message = "차량 종류를 입력해주세요.") String vehicleType) {
}
