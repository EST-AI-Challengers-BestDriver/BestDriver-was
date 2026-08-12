CREATE TABLE route_request (
    route_request_id UUID PRIMARY KEY,
    origin_name VARCHAR(100) NOT NULL,
    destination_name VARCHAR(100) NOT NULL,
    vehicle_type VARCHAR(20),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE route_candidate (
    route_candidate_id UUID PRIMARY KEY,
    route_request_id UUID NOT NULL,
    provider VARCHAR(20) NOT NULL,
    provider_option VARCHAR(30) NOT NULL,
    distance_m INTEGER NOT NULL,
    duration_sec INTEGER NOT NULL,
    toll_fare INTEGER,
    path JSONB NOT NULL,
    is_baseline BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_route_candidate_request
        FOREIGN KEY (route_request_id)
        REFERENCES route_request (route_request_id)
        ON DELETE CASCADE
);

CREATE TABLE energy_prediction (
    energy_prediction_id UUID PRIMARY KEY,
    route_candidate_id UUID NOT NULL,
    route_request_id UUID NOT NULL,
    model_version VARCHAR(30) NOT NULL,
    predicted_energy DECIMAL NOT NULL,
    model_output JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_energy_prediction_candidate
        FOREIGN KEY (route_candidate_id)
        REFERENCES route_candidate (route_candidate_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_energy_prediction_request
        FOREIGN KEY (route_request_id)
        REFERENCES route_request (route_request_id)
        ON DELETE CASCADE
);

CREATE TABLE route_recommendation (
    route_recommendation_id UUID PRIMARY KEY,
    route_request_id UUID NOT NULL,
    baseline_candidate_id UUID NOT NULL,
    saved_energy_percent DECIMAL NOT NULL,
    saved_energy DECIMAL NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_route_recommendation_request
        FOREIGN KEY (route_request_id)
        REFERENCES route_request (route_request_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_route_recommendation_baseline
        FOREIGN KEY (baseline_candidate_id)
        REFERENCES route_candidate (route_candidate_id)
);

CREATE INDEX idx_route_request_route
    ON route_request (origin_name, destination_name, status);

CREATE INDEX idx_route_candidate_request
    ON route_candidate (route_request_id);

CREATE INDEX idx_energy_prediction_request
    ON energy_prediction (route_request_id);
