package com.dolloer.colla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CollaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollaApplication.class, args);
    }

}
