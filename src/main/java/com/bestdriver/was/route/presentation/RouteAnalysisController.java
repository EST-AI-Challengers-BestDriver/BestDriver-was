package com.bestdriver.was.route.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bestdriver.was.route.application.RouteAnalysisService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/route-analyses")
public class RouteAnalysisController {

    private final RouteAnalysisService routeAnalysisService;

    public RouteAnalysisController(RouteAnalysisService routeAnalysisService) {
        this.routeAnalysisService = routeAnalysisService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RouteAnalysisResponse analyze(@Valid @RequestBody RouteAnalysisRequest request) {
        return routeAnalysisService.analyze(request);
    }
}
