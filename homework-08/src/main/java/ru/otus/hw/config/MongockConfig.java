package ru.otus.hw.config;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "ru.otus.hw.repositories")
@EnableMongock
@Profile("!test")
public class MongockConfig {
}