package adhd.diary.global.config;

import adhd.diary.chatgpt.util.ChatGptUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatGptConfig {

    @Bean
    public ChatGptUtil chatGptUtil() {
        return new ChatGptUtil();
    }
}
