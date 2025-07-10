package com.mohdbam.spring_ai_playground.output;

public record Activity(
    String name,
    String description,
    String timeOfDay,
    String location,
    String duration,
    String cost
) {

}
