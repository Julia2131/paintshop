package com.example.paintmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.format.DateTimeFormatter;

@Configuration
public class FormatterConfig {
    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        // pattern tuỳ ý bạn
        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    }
}