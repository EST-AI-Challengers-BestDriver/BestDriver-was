INSERT INTO route_request (
    route_request_id,
    origin_name,
    destination_name,
    vehicle_type,
    status,
    created_at
) VALUES (
    '10000000-0000-0000-0000-000000000001',
    '부산',
    '서울',
    NULL,
    'TEMPLATE',
    TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+09:00'
);

INSERT INTO route_candidate (
    route_candidate_id,
    route_request_id,
    provider,
    provider_option,
    distance_m,
    duration_sec,
    toll_fare,
    path,
    is_baseline,
    created_at
) VALUES
    (
        '20000000-0000-0000-0000-000000000001',
        '10000000-0000-0000-0000-000000000001',
        'DEMO', '최단시간', 397400, 16680, 25300, '[]', TRUE,
        TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:01+09:00'
    ),
    (
        '20000000-0000-0000-0000-000000000002',
        '10000000-0000-0000-0000-000000000001',
        'DEMO', '최단거리', 389200, 18300, 18800, '[]', FALSE,
        TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:02+09:00'
    ),
    (
        '20000000-0000-0000-0000-000000000003',
        '10000000-0000-0000-0000-000000000001',
        'DEMO', '에코 후보', 405800, 17520, 23000, '[]', FALSE,
        TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:03+09:00'
    );
