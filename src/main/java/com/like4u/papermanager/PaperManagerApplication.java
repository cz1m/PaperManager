package com.like4u.papermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PaperManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperManagerApplication.class, args);
    }

}
