package com.mohdbam.spring_ai_playground.multimodal;

import java.util.Map;

import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
public class ImageGeneration {

    private final OpenAiImageModel openAiImageModel;

    // this endpoint wasn't tested, because I don't have an OpenAI API tokens
    @GetMapping("/generate-image")
    public ResponseEntity<Map<String, String>> getMethodName(@RequestParam String prompt) {
        ImageOptions options = OpenAiImageOptions.builder()
                .model("dall-e-3")
                .width(1024)
                .height(1024)
                .quality("hd")
                .style("vivid")
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(prompt, options);
        ImageResponse imageResponse = openAiImageModel.call(imagePrompt);

        String url = imageResponse.getResult().getOutput().getUrl();

        return ResponseEntity.ok(Map.of(
                "prompt", prompt,
                "imageUrl", url));
    }

}
