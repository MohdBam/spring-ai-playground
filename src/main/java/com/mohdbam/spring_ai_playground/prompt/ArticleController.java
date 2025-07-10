package com.mohdbam.spring_ai_playground.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ChatClient chatClient;

    public ArticleController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/posts/new")
    public Flux<String> newPost(@RequestParam(defaultValue = "JDK Virtual Threads") String topic) {
        String systemInstruction = """
        Write a well-structured, informative article.

The article should include:

A compelling introduction that explains why this topic is important or relevant today.

Clear subheadings that break down key aspects of the topic.

At least 3–5 main sections, each with informative and engaging content.

Use real-world examples, statistics, or anecdotes where appropriate.

A conclusion that summarizes the main points and leaves the reader with a clear takeaway or call to action.

Style Guidelines:

Write in a clear and professional tone.

Use simple, engaging language suitable for a general audience.

Avoid jargon unless necessary, and explain any complex terms.

Keep paragraphs short and readable.

Target audience: [Define your audience — e.g., general readers, business professionals, tech enthusiasts, students, etc.]

Suggested word count: 800–1000 words.
""";
        return chatClient.prompt()
        .user(u -> {
            u.text("Write me a blog post about {topic}");
            u.param("topic", topic);
        })
        .system(systemInstruction)
        .stream()
        .content();
    }

}
