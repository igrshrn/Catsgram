package ru.yandex.practicum.catsgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CatsgramApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CatsgramApplication.class, args);
    }
}