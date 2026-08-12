package com.bestdriver.was.route.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bestdriver.was.route.presentation.RouteAnalysisRequest;
import com.bestdriver.was.route.presentation.RouteAnalysisResponse;

@SpringBootTest
class RouteAnalysisServiceTests {

    @Autowired
    private RouteAnalysisService routeAnalysisService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void analyzesSeededBusanToSeoulRoutesWithoutPersistingHistory() {
        int requestsBefore = countRows("route_request");
        int predictionsBefore = countRows("energy_prediction");
        int recommendationsBefore = countRows("route_recommendation");

        RouteAnalysisResponse response = routeAnalysisService.analyze(
                new RouteAnalysisRequest("부산역", "서울역", "트럭"));

        assertThat(response.start()).isEqualTo("부산역");
        assertThat(response.destination()).isEqualTo("서울역");
        assertThat(response.vehicleType()).isEqualTo("트럭");
        assertThat(response.predictionMode()).isEqualTo("MOCK");
        assertThat(response.routes()).hasSize(3);
        assertThat(response.recommendedRouteId()).isNotBlank();
        assertThat(response.baselineRouteId())
                .isEqualTo("20000000-0000-0000-0000-000000000001");
        assertThat(response.savedEnergy()).isPositive();
        assertThat(countRows("route_request")).isEqualTo(requestsBefore);
        assertThat(countRows("energy_prediction")).isEqualTo(predictionsBefore);
        assertThat(countRows("route_recommendation")).isEqualTo(recommendationsBefore);
    }

    @Test
    void rejectsUnsupportedRouteRequest() {
        RouteAnalysisRequest request = new RouteAnalysisRequest("제주", "서울", "SUV");

        assertThatThrownBy(() -> routeAnalysisService.analyze(request))
                .isInstanceOf(RouteRequestNotFoundException.class)
                .hasMessageContaining("제주 → 서울");
    }

    private int countRows(String tableName) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
        return count == null ? 0 : count;
    }
}
