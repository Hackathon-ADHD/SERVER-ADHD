package adhd.diary.chatgpt.util;

import adhd.diary.chatgpt.dto.ChatCompletionRequest;
import adhd.diary.chatgpt.dto.ChatCompletionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;

public class ChatGptUtil {

    private final HttpClient client = HttpClient.newBuilder().build();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${openai.url.base}")
    private String API_BASE_URL;

    @Value("${chatgpt.api-key}")
    private String API_KEY;

    public CompletableFuture<String> createChatCompletion(ChatCompletionRequest requestBody, String API_URL) {
        String jsonBody = jsonParsing(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenCompose(this::extractChatCompletionResponse)
                .exceptionally(e -> "Error: " + e.getMessage());
    }

    public String jsonParsing(ChatCompletionRequest request) {
        try {
            return mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Json 변환 실패");
        }
    }

    public CompletableFuture<String> extractChatCompletionResponse(String response) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return extractAnswer(mapper.readValue(response, ChatCompletionResponse.class));
            } catch (Exception e) {
                System.err.println("Failed to parse JSON: " + e.getMessage());
                return null;
            }
        });
    }

    public String extractAnswer(ChatCompletionResponse response) {
        try {
            return mapper.writeValueAsString(response.choices().get(0).getMessage().getContent());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to parse JSON: " + e.getMessage());
        }
    }
}
