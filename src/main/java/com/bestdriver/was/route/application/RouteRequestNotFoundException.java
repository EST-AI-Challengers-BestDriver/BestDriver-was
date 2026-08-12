package com.bestdriver.was.route.application;

public class RouteRequestNotFoundException extends RuntimeException {

    public RouteRequestNotFoundException(String start, String destination) {
        super("지원하는 경로가 아닙니다: " + start + " → " + destination);
    }
}
