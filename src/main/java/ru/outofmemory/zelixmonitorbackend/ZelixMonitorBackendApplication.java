package ru.outofmemory.zelixmonitorbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZelixMonitorBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZelixMonitorBackendApplication.class, args);
    }

}
