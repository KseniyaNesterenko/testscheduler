package ru.itmo.testscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TestschedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestschedulerApplication.class, args);
    }

}
