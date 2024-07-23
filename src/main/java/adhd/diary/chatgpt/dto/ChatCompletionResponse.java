package adhd.diary.chatgpt.dto;

import java.util.List;

public class ChatCompletionResponse {

    private String id;

    private String object;

    private long created;

    private String model;

    private List<ChatCompletionChoice> choices;

    private Usage usage;

    public ChatCompletionResponse(String id,
                                  String object,
                                  long created,
                                  String model,
                                  List<ChatCompletionChoice> choices,
                                  Usage usage) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.choices = choices;
        this.usage = usage;
    }

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public long getCreated() {
        return created;
    }

    public String getModel() {
        return model;
    }

    public List<ChatCompletionChoice> getChoices() {
        return choices;
    }

    public Usage getUsage() {
        return usage;
    }
}
