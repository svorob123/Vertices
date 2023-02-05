package com.task.vertices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@ComponentScan({"com.task.vertices.service", "com.task.vertices.conf"})
public class VerticesApplication {

    public static void main(String[] args) {
        SpringApplication.run(VerticesApplication.class, args);
    }

}
