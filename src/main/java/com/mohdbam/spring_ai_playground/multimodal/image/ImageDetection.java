package com.mohdbam.spring_ai_playground.multimodal.image;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.SneakyThrows;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/image-detection")
public class ImageDetection {

    private final ChatClient chatClient;

    Resource sampleImage = new ClassPathResource("images/image.jpg");

    public ImageDetection(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(OllamaOptions.builder()
                        .model("llava:7b")
                        .build())
                .build();
    }

    @GetMapping("/image-to-text")
    public Flux<String> imageToText() {
        return chatClient.prompt()
                // .options(OllamaOptions.builder()
                // .model("llava:7b").build())
                .user(u -> {
                    u.text("Describe the content of this image.");
                    u.media(MimeTypeUtils.IMAGE_JPEG, sampleImage);
                })
                .system("You are an image analysis assistant. Describe the content of the provided image in detail.")
                .stream()
                .content();
    }
}
