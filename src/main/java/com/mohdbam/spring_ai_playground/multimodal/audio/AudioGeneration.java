package com.mohdbam.spring_ai_playground.multimodal.audio;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.AudioResponseFormat;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class AudioGeneration {

    private final OpenAiAudioSpeechModel audioSpeechModel;

    // can't be tested because I don't have OpenAi tokens
    @GetMapping("/speak")
    public ResponseEntity<byte[]> speak(@RequestParam String message) {

        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .model(OpenAiAudioApi.TtsModel.TTS_1_HD.name())
                .responseFormat(AudioResponseFormat.MP3)
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .build();

        SpeechPrompt speechPrompt = new SpeechPrompt(message, options);

        SpeechResponse response = audioSpeechModel.call(speechPrompt);

        byte[] audioContent = response.getResult().getOutput();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.mp3\"")
                .body(audioContent);
    }
    

}
