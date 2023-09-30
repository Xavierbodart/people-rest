package com.box.people.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BoxPeopleRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoxPeopleRestApplication.class, args);
    }

}
