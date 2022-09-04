package com.dotdash.recruiting.bookreview.server;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@EnableAdminServer
@Configuration
@EnableAutoConfiguration
@Profile("server")
@SpringBootApplication
public class BookReviewApplicationServer {

    public static void main(String[] args) {
        SpringApplication server = new SpringApplication(BookReviewApplicationServer.class);
        server.setAdditionalProfiles("server");
        server.run(args);
    }
}