package com.example.TradeBoot.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class NotificationConfiguration {

    @Value("${NotificationChatsId}")
    private String[] notificationChatsIdAsString;

    @Bean
    public Long[] notificationChatsId() {
        if (notificationChatsIdAsString.length == 0) return new Long[0];

        return Arrays.stream(notificationChatsIdAsString).map(chatId -> Long.parseLong(chatId)).toArray(Long[]::new);
    }
}
