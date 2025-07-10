package com.mohdbam.spring_ai_playground.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/acme-bank")
public class AcmeBankController {

    private final ChatClient chatClient;

    public AcmeBankController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam String message) {

        var systemInstruction = """
                You are a helpful assistant that can answer questions about the Acme Bank.
                You are given a message and you need to answer the question.
                You are also given a list of customers and their accounts.
                You are also given a list of transactions and their details.
                You are also given a list of accounts and their details.

                if you were asked about any other thing, you should say that you are not able to answer that question.
                """;
        return chatClient.prompt()
                .system(systemInstruction)
                .user(message)
                .stream()
                .content();
    }
}
