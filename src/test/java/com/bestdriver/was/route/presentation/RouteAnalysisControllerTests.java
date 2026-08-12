package com.bestdriver.was.route.presentation;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RouteAnalysisControllerTests {

    @LocalServerPort
    private int port;

    @Test
    void returnsFrontendCompatibleRouteAnalysisJson() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + port + "/api/v1/route-analyses"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("""
                        {
                          "start": "부산",
                          "destination": "서울",
                          "vehicleType": "SUV"
                        }
                        """))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.body())
                .contains("\"predictionMode\":\"MOCK\"")
                .contains("\"baselineRouteId\":\"20000000-0000-0000-0000-000000000001\"")
                .contains("\"isRecommended\":true")
                .contains("\"energyUnit\":\"kWh\"");
    }
}
