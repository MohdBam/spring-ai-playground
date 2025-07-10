package com.mohdbam.spring_ai_playground.output;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/vacation-plan")
public class VacationPlan {

    private ChatClient chatClient;

    public VacationPlan(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/unstructured")
    public Flux<String> unstructured() {
        return chatClient.prompt()
                .user(u -> u.text("Create a vacation plan for me."))
                .system("""
                        Create a detailed vacation plan for a family of four, including:
                        
                        1. Destination: Choose a family-friendly location.
                        2. Duration: Specify the length of the vacation.
                        3. Accommodation: Suggest suitable lodging options.
                        4. Activities: Include a mix of relaxation and adventure suitable for all ages.
                        5. Budget: Provide an estimated budget breakdown.
                        
                        Ensure the plan is engaging, practical, and tailored to a family setting.
                        """)
                .stream()
                .content();
    }

    @GetMapping("/structured")
    public ResponseEntity<?> structured() {
        try {
            Itinerary itinerary = chatClient.prompt()
                    .user(u -> u.text("Create a structured vacation plan for me."))
                    .system("""
                            You are a travel planning assistant. Create a vacation plan and return it as a valid JSON object.
                            
                            IMPORTANT: You must respond with ONLY valid JSON. Do not include any HTML, markdown, or other formatting.
                            
                            The JSON should have this exact structure:
                            {
                              "destination": "Family-friendly location name",
                              "duration": "Length of vacation (e.g., 7 days)",
                              "accommodation": "Lodging description",
                              "budget": "Estimated budget (e.g., $2000-$3000)",
                              "activities": [
                                {
                                  "name": "Activity name",
                                  "description": "Activity description",
                                  "timeOfDay": "Morning/Afternoon/Evening",
                                  "location": "Activity location",
                                  "duration": "Activity duration",
                                  "cost": "Activity cost"
                                }
                              ]
                            }
                            
                            Create a vacation plan for a family of four with 3-5 activities.
                            """)
                    .call()
                    .entity(Itinerary.class);
            
            return ResponseEntity.ok(itinerary);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error generating vacation plan: " + e.getMessage());
        }
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<String> handleJsonParseException(JsonParseException e) {
        return ResponseEntity.badRequest()
                .body("Error parsing AI response as JSON. The AI model may have returned invalid format. Please try again.");
    }
}
