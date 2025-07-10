package com.mohdbam.spring_ai_playground.output;

import java.util.List;

public record Itinerary(
    List<Activity> activities,
    String destination,
    String duration,
    String accommodation,
    String budget
) {

}
